package com.example.springapp.controllers;

import io.github.resilience4j.ratelimiter.RateLimiter;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.example.springapp.service.WeatherService.getWebClientCurrentWeatherInCity;


@RestController
@RequestMapping("/api/weather/get")
@AllArgsConstructor
public class ControllerApiClient {
    @Autowired
    private final WebClient webClient;
    @Autowired
    private final RateLimiter rateLimiter;

    @GetMapping("/{city}")
    public Mono<Object> getCurrentWeatherInCity(@PathVariable String city) {
        return  getWebClientCurrentWeatherInCity(city, webClient,rateLimiter);
    }
}
