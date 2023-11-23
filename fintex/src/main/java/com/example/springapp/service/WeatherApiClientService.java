package com.example.springapp.service;


import com.example.springapp.cache.Cache;
import com.example.springapp.cache.Node;
import com.example.springapp.database.DAO.WeathDao;
import com.example.springapp.database.JDBC.WeatherDaoJdbc;
import com.example.springapp.database.entity.Weather;
import com.example.springapp.exceptions.webClient.WeatherApiErrorsDTO;
import com.example.springapp.exceptions.webClient.WebClientExceptionFactory;
import com.example.springapp.exceptions.webClient.WebClientRequestLimitExceededException;
import com.example.springapp.mappers.WeatherMapper;
import io.github.resilience4j.ratelimiter.RateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.sql.SQLException;


@Component
public class WeatherApiClientService {
    @Value("${weather-api.token}")
    String token;

    private final Cache<Weather> weatherCache;

    private final WeathDao weathDao;
    @Autowired
    WeatherDaoJdbc weatherDaoJdbc;

    @Autowired
    RateLimiter rateLimiter;
    @Autowired
    WebClient webClient;

    public WeatherApiClientService(@Qualifier("weatherDaoJdbc") WeathDao weathDao, @Qualifier("cacheWeather") Cache<Weather> weatherCache) {
        this.weathDao = weathDao;
        this.weatherCache = weatherCache;
    }

    public CurrentWeatherDTO getWebClientCurrentWeatherInCity(String city) {
        if (!rateLimiter.acquirePermission()) {
            //Приведение типа, чтобы не ругалось на возвращаемый тип
            return (CurrentWeatherDTO) Mono.error(new WebClientRequestLimitExceededException()).block();
        }
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("key", token)
                        .queryParam("q", city)
                        .build())
                .retrieve()
                .onStatus(
                        httpStatusCode -> httpStatusCode.is4xxClientError() || httpStatusCode.is5xxServerError(),
                        this::webClientGetException
                        )
                .bodyToMono(CurrentWeatherDTO.class).block();
    }

    public Weather getCurrentWeatherCache(String location) throws SQLException {
        CurrentWeatherDTO jsonCurrentWeather = getWebClientCurrentWeatherInCity(location);
        Weather weather = new WeatherMapper().of(jsonCurrentWeather);

        if(weatherCache.keysMap.containsKey(location)){
            Node<Weather> node =  weatherCache.keysMap.get(location);
            if(node.value.getDateTimeMeasurement().compareTo(weather.getDateTimeMeasurement()) != 0){
                weatherCache.keysMap.remove(location);
                //Помещаем объект в кэш
                return weatherCache.get(location,()-> weather);
            }
            //Получаем из кэша
            return weatherCache.get(location,()-> weather);
        }

        Weather findWeather = weatherDaoJdbc.getWeatherForCityNameAndDateTimeMeasurement(weather);

        if (findWeather.getId() == null) {
            Weather createdWeather = weathDao.save(weather);
            //Помещаем в кэш
            return weatherCache.get(location,()-> createdWeather);
        }
        //Помещаем в кэш
        return weatherCache.get(location,()-> findWeather);
    }

    private Mono<? extends Throwable> webClientGetException(ClientResponse response){
        return response.bodyToMono(WeatherApiErrorsDTO.class).mapNotNull(x -> {
            int errorCode = x.getError().getCode();
            WebClientExceptionFactory.getException(errorCode);
            return null;
        });
    }
}

