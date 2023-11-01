package com.example.springapp.service;

import com.example.springapp.exceptions.webClient.WeatherApiErrorsDTO;
import com.example.springapp.exceptions.webClient.WebClientExceptionFactory;
import com.example.springapp.exceptions.webClient.WebClientRequestLimitExceededException;
import io.github.resilience4j.ratelimiter.RateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Component
public class WeatherApiClientService {
    @Autowired
    Environment env;
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


    private Mono<? extends Throwable> webClientGetException(ClientResponse response){
        return response.bodyToMono(WeatherApiErrorsDTO.class).mapNotNull(x -> {
            int errorCode = x.getError().getCode();
            WebClientExceptionFactory.getException(errorCode);
            return null;
        });
    }
}
