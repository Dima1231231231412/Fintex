package com.example.springapp.controllers;

import com.example.springapp.database.entity.Weather;
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
    WebClient webClient;
    @Autowired
    WeatherApiClientService weatherApiClientService;

    public ControllerApiClient(WebClient webClient) {
        this.webClient = webClient;
    }


    @GetMapping("/{city}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public Weather getAndAddCurrentWeatherInCity(@PathVariable String city) throws SQLException {
        return weatherApiClientService.getCurrentWeatherCache(city);
    }
}
