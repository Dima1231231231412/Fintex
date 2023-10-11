package com.example.springapp.controllers;

import com.example.springapp.service.CurrentWeatherDTO;
import com.example.springapp.service.WeatherApiClientService;
import io.github.resilience4j.ratelimiter.RateLimiter;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/weather/get")
@AllArgsConstructor
public class ControllerApiClient {
    @Autowired
    private final WebClient webClient;
    @Autowired
    private final RateLimiter rateLimiter;
    @Autowired
    WeatherApiClientService weatherApiClientService;

    @GetMapping("/{city}")
    public Mono<CurrentWeatherDTO> getCurrentWeatherInCity(@PathVariable String city, @Value("${weather-api.token}") String token) {
        return  weatherApiClientService.getWebClientCurrentWeatherInCity(city, webClient,rateLimiter,token);
    }
}
