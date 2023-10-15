package com.example.springapp.service;

import com.example.springapp.exceptions.webClient.WeatherApiErrorsDTO;
import com.example.springapp.exceptions.webClient.WebClientExceptionFactory;
import com.example.springapp.exceptions.webClient.WebClientRequestLimitExceededException;
import io.github.resilience4j.ratelimiter.RateLimiter;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Component
public class WeatherApiClientService {
    public Mono<CurrentWeatherDTO> getWebClientCurrentWeatherInCity(String city, WebClient webClient, RateLimiter rateLimiter,String token) {
        if (!rateLimiter.acquirePermission()) {
            return Mono.error(new WebClientRequestLimitExceededException());
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
                .bodyToMono(CurrentWeatherDTO.class);
    }


    private Mono<? extends Throwable> webClientGetException(ClientResponse response){
        return response.bodyToMono(WeatherApiErrorsDTO.class).mapNotNull(x -> {
            int errorCode = x.getError().getCode();
            WebClientExceptionFactory.getException(errorCode);
            return null;
        });
    }
}
