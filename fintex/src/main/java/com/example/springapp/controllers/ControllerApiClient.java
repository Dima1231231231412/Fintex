package com.example.springapp.controllers;

import com.example.springapp.service.CurrentWeatherDTO;
import com.example.springapp.service.WeatherApiClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.sql.SQLException;

@RestController
@RequestMapping("/api/weather/get")
public class ControllerApiClient {
    @Autowired
    private final WebClient webClient;
    @Autowired
    WeatherApiClientService weatherApiClientService;

    public ControllerApiClient(WebClient webClient,
                               WeatherApiClientService weatherApiClientService) {
        this.webClient = webClient;
        this.weatherApiClientService = weatherApiClientService;
    }


    @GetMapping("/{city}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public CurrentWeatherDTO getAndAddCurrentWeatherInCity(@PathVariable String city) throws SQLException {
        return weatherApiClientService.addCurrentWeather(city, webClient);
    }
}
