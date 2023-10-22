package com.example.springapp.controllers;

import com.example.springapp.WebClientApiWeatherDTO;
import com.example.springapp.database.DAO.WeatherJdbcDAO;
import com.example.springapp.service.CurrentWeatherDTO;
import com.example.springapp.service.WeatherApiClientService;
import io.github.resilience4j.ratelimiter.RateLimiter;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.sql.SQLException;
import java.sql.Timestamp;

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
    @Autowired
    WebClientApiWeatherDTO webClientApiWeatherDTO;
    @Autowired
    WeatherJdbcDAO weatherJDBCDAO;

    @GetMapping("/{city}")
    public Mono<CurrentWeatherDTO> getCurrentWeatherInCity(@PathVariable String city, @Value("${weather-api.token}") String token) throws SQLException {
        Mono<CurrentWeatherDTO> jsonCurrentWeather = weatherApiClientService.getWebClientCurrentWeatherInCity(city, webClient, rateLimiter, token);
        webClientApiWeatherDTO = webClientApiWeatherDTO.of(jsonCurrentWeather.block());
        System.out.println(weatherJDBCDAO.createWeather(webClientApiWeatherDTO));
        System.out.println(weatherJDBCDAO.updateWeather(2,"Moscow", -17, "test23", Timestamp.valueOf("1980-05-05 30:00:15")));
        return jsonCurrentWeather;
    }
}
