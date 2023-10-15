package com.example.springapp.service;

import com.example.springapp.exceptions.ErrorResponse;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.springapp.service.FactoryWeather.mapIdReg;

@Component
public class WeatherService {

    public Map<LocalDateTime,Integer> getTemperatureForDayInCity(String city)  {
        if(!mapIdReg.containsKey(city))
            throw ErrorResponse.notFoundCity();

        Map<LocalDateTime,Integer> map = WeatherDao.weatherList.stream()
                .filter(weather -> weather.getReg().equals(city) &&
                        weather.getDateTime().toLocalDate().compareTo(LocalDate.now()) == 0)
                .collect(Collectors.toMap(Weather::getDateTime, Weather::getTemp));
        if(!map.isEmpty()) {
            return map;
        }
        throw ErrorResponse.IsNoRecordWithTempForToday(city);


    }


    public Weather addNewRecordWeather(String city, Weather weather) throws ParseException {
        FactoryWeather fw = new FactoryWeather();
        Weather newRecordWeather = fw.createWeather(
                city,
                weather.getTemp(),
                weather.getDateTime().toString()
        );
        WeatherDao.weatherList.add(newRecordWeather);
        return newRecordWeather;
    }

    public Weather updateTemperatureInCity(String city,Weather weather) throws ParseException {
        int index = Integer.MAX_VALUE;
        for (int i = 0; i < WeatherDao.weatherList.size(); i++) {
            if (WeatherDao.weatherList.get(i).getReg().equals(city) && WeatherDao.weatherList.get(i).getDateTime().compareTo(weather.getDateTime()) == 0)
                index = i;
        }

        if (index == Integer.MAX_VALUE) {
            FactoryWeather fw = new FactoryWeather();
            Weather newWeather = fw.createWeather(city, weather.getTemp(), weather.getDateTime().toString());
            WeatherDao.weatherList.add(newWeather);
            return newWeather;
        }
        WeatherDao.weatherList.get(index).setTemp(weather.getTemp());
        return WeatherDao.weatherList.get(index);
    }


    public void deleteCityInformation (String city) {
        if(!mapIdReg.containsKey(city))
            throw ErrorResponse.notFoundCity();

        WeatherDao.weatherList.removeIf(weather -> weather.getReg().equals(city));
        mapIdReg.remove(city);

    }
}
