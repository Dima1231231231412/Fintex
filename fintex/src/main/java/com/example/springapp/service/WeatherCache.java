package com.example.springapp.service;

import com.example.springapp.database.JDBC.WeatherDaoJdbc;
import com.example.springapp.database.entity.Weather;
import com.example.springapp.mappers.WeatherMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.WeakHashMap;

@Component
public class WeatherCache {
    @Autowired
    WeatherApiClientService weatherApiClientService;
    @Autowired
    WeatherDaoJdbc weatherDaoJdbc;
    @Value("${cache.course.size}")
    private int maxSize;
    private final WeakHashMap<String, Weather> lrucache = new WeakHashMap<>(maxSize, 0.75f);
    //Счётчик, фиксирующий обращение к каждому городу
    private final LinkedHashMap<String, Integer> accessCount = new LinkedHashMap<>(maxSize, 0.75f, true);
    private final long fifteenMinutesInMillis = 900000;


    public synchronized Weather getWeather(String city, WebClient webClient) throws SQLException {
        CurrentWeatherDTO jsonCurrentWeather = weatherApiClientService.getWebClientCurrentWeatherInCity(city, webClient);
        Weather weather = new WeatherMapper().of(jsonCurrentWeather);

        String location = weather.getCityName();
        if (lrucache.containsKey(location)) {
            //При получение данных с API достаточно сравнить дату и время погоды (обновляется раз в 15 минут)
           if(!checkDateDifferenceIsfifteenMinutes(lrucache.get(location).getDateTimeMeasurement(),weather.getDateTimeMeasurement())){
               lrucache.remove(location);
               lrucache.put(location,weather);
               return lrucache.get(location);
           }

           accessCount.put(location, accessCount.get(location) + 1);
           return lrucache.get(location);
        }

        Weather findWeather = fetchWeatherFromDatabase(weather);
        //Если запись с погодой не найдена
        if (findWeather.getId() == null) {
            weather = weatherDaoJdbc.save(weather);
            updateCache(location, weather);
            return weather;
        }
        weather = findWeather;
        updateCache(location, findWeather);
        return weather;
    }

    public boolean checkDateDifferenceIsfifteenMinutes(Timestamp cacheWeatherTimestamp, Timestamp currentWeatherTimestamp){
        long differenceInMillis = currentWeatherTimestamp.getTime() - cacheWeatherTimestamp.getTime();
        return differenceInMillis < fifteenMinutesInMillis;
    }
    public Weather fetchWeatherFromDatabase(Weather weather) throws SQLException {
        return weatherDaoJdbc.getWeatherForCityNameAndDateTimeMeasurement(weather);
    }

    private void updateCache(String location, Weather weather) {
        if (lrucache.size() >= maxSize) {
            evictLeastRecentlyUsed();
        }
        lrucache.put(location, weather);
        accessCount.put(location, 1);
    }
    //Удаляем из кэша объекты погоды, с наименьшим использованием
    private void evictLeastRecentlyUsed() {
        String leastUsedLocation = accessCount.entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
        if (leastUsedLocation != null) {
            lrucache.remove(leastUsedLocation);
            accessCount.remove(leastUsedLocation);
        }
    }
}
