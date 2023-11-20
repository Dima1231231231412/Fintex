package com.example.springapp.service;

import com.example.springapp.cache.LruListNode;
import com.example.springapp.cache.WeatherCache;
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

    private final WeathDao weathDao;
    @Autowired
    WeatherDaoJdbc weatherDaoJdbc;

    @Autowired
    RateLimiter rateLimiter;
    @Autowired
    WebClient webClient;

    public WeatherApiClientService(@Qualifier("weatherDaoJdbc") WeathDao weathDao) {
        this.weathDao = weathDao;
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

        WeatherCache<Weather> weatherCache = new WeatherCache<>();

        if(weatherCache.lrucache.containsKey(location)) {
            LruListNode<Weather> node = weatherCache.lrucache.get(location);
            if (node.nodeValue.getDateTimeMeasurement().compareTo(weather.getDateTimeMeasurement()) != 0) {
                LruListNode<Weather> newNode = new LruListNode<>(weather);

                weatherCache.remove(location);
                weatherCache.update(location,newNode);

                weatherCache.accessCount.put(location, weatherCache.accessCount.get(location) + 1);
                return newNode.nodeValue;
            }

            weatherCache.accessCount.put(location, weatherCache.accessCount.get(location) + 1);
            return node.nodeValue;
        }

        Weather findWeather = weatherDaoJdbc.getWeatherForCityNameAndDateTimeMeasurement(weather);
        if (findWeather.getId() == null) {
            weather = weathDao.save(weather);
            weatherCache.update(location, weather);
            return weather;
        }

        weatherCache.update(location, findWeather);
        return findWeather;
    }

    private Mono<? extends Throwable> webClientGetException(ClientResponse response){
        return response.bodyToMono(WeatherApiErrorsDTO.class).mapNotNull(x -> {
            int errorCode = x.getError().getCode();
            WebClientExceptionFactory.getException(errorCode);
            return null;
        });
    }
}

