package com.example.springapp.controllers;

import com.example.springapp.database.entity.Weather;
import com.example.springapp.service.WeatherApiClientService;
import com.example.springapp.service.WeatherCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.sql.SQLException;

@RestController
@RequestMapping("/api/currentWeather")
public class CurrentWeatherCacheController {
    @Autowired
    private final WebClient webClient;
    @Autowired
    WeatherCache weatherCache;
    @Autowired
    WeatherApiClientService weatherApiClientService;

    public CurrentWeatherCacheController(WebClient webClient,
                                         WeatherCache weatherCache,
                                         WeatherApiClientService weatherApiClientService) {
        this.webClient = webClient;
        this.weatherCache = weatherCache;
        this.weatherApiClientService = weatherApiClientService;
    }


    @GetMapping("/{city}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public Weather getAndAddCurrentWeatherInCity(@PathVariable String city) throws SQLException {
        return weatherCache.getWeather(city, webClient);
    }
}
