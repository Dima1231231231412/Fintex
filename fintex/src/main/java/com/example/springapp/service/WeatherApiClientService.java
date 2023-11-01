package com.example.springapp.service;

import com.example.springapp.WeatherMapper;
import com.example.springapp.database.DAO.WeathDao;
import com.example.springapp.database.entity.Weather;
import com.example.springapp.exceptions.webClient.WeatherApiErrorsDTO;
import com.example.springapp.exceptions.webClient.WebClientExceptionFactory;
import com.example.springapp.exceptions.webClient.WebClientRequestLimitExceededException;
import io.github.resilience4j.ratelimiter.RateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.sql.SQLException;


@Component
public class WeatherApiClientService {
    @Autowired
    Environment env;
    private final WeathDao weathDao;

    public WeatherApiClientService(@Qualifier("weatherDaoJdbc") WeathDao weathDao) {
        this.weathDao = weathDao;
    }

    public CurrentWeatherDTO getWebClientCurrentWeatherInCity(String city, WebClient webClient, RateLimiter rateLimiter) {
        if (!rateLimiter.acquirePermission()) {
            //Приведение типа, чтобы не ругалось на возвращаемый тип
            return (CurrentWeatherDTO) Mono.error(new WebClientRequestLimitExceededException()).block();
        }
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("key", env.getProperty("weather-api.token"))
                        .queryParam("q", city)
                        .build())
                .retrieve()
                .onStatus(
                        httpStatusCode -> httpStatusCode.is4xxClientError() || httpStatusCode.is5xxServerError(),
                        this::webClientGetException
                        )
                .bodyToMono(CurrentWeatherDTO.class).block();
    }

    public CurrentWeatherDTO addCurrentWeather(String city, WebClient webClient, RateLimiter rateLimiter) throws SQLException {
        CurrentWeatherDTO jsonCurrentWeather = getWebClientCurrentWeatherInCity(city, webClient, rateLimiter);
        Weather weather = new WeatherMapper().of(jsonCurrentWeather);
        weathDao.save(weather);
        return jsonCurrentWeather;
    }


    private Mono<? extends Throwable> webClientGetException(ClientResponse response){
        return response.bodyToMono(WeatherApiErrorsDTO.class).mapNotNull(x -> {
            int errorCode = x.getError().getCode();
            WebClientExceptionFactory.getException(errorCode);
            return null;
        });
    }
}
