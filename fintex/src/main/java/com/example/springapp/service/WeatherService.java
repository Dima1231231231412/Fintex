package com.example.springapp.service;

import com.example.springapp.exceptions.ErrorResponse;
import com.example.springapp.exceptions.WebClientErrorCodeFactory;
import com.example.springapp.exceptions.WebClientExceptions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.swagger.v3.core.util.Json;
import jakarta.validation.constraints.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.springapp.configs.WebClientConfig.apiKey;
import static com.example.springapp.service.FactoryWeather.mapIdReg;

@Component
public class WeatherService {


    public  Map<LocalDateTime,Integer> getTemperatureForDayInCity(String city)  {
        if(!mapIdReg.containsKey(city))
            throw ErrorResponse.notFoundCity();

        Map<LocalDateTime,Integer> map = WeatherDao.weatherList.stream()
                .filter(weather -> weather.getReg().equals(city) &&
                        weather.getDateTime().toLocalDate().compareTo(LocalDate.now()) == 0)
                .collect(Collectors.toMap(Weather::getDateTime, Weather::getTemp));
        if(map.isEmpty())
            throw ErrorResponse.IsNoRecordWithTempForToday(city);
        else
            return map;

    }


    public  Weather addNewRecordWeather(String city, Weather weather) throws ParseException {
        FactoryWeather fw = new FactoryWeather();
        Weather newRecordWeather = fw.createWeather(
                city,
                weather.getTemp(),
                weather.getDateTime().toString()
        );
        WeatherDao.weatherList.add(newRecordWeather);
        return newRecordWeather;
    }

    public  Weather updateTemperatureInCity(String city,Weather weather) throws ParseException {
        int index = Integer.MAX_VALUE;
        for (int i = 0; i < WeatherDao.weatherList.size(); i++) {
            if (WeatherDao.weatherList.get(i).getReg().equals(city) && WeatherDao.weatherList.get(i).getDateTime().compareTo(weather.getDateTime()) == 0)
                index = i;
        }

        if (index != Integer.MAX_VALUE) {
            WeatherDao.weatherList.get(index).setTemp(weather.getTemp());
            return WeatherDao.weatherList.get(index);
        } else {
            FactoryWeather fw = new FactoryWeather();
            Weather newWeather  =  fw.createWeather(city, weather.getTemp(), weather.getDateTime().toString());
            WeatherDao.weatherList.add(newWeather);
            return newWeather;
        }
    }

    public static Mono<Object> getWebClientCurrentWeatherInCity(String city, WebClient webClient, RateLimiter rateLimiter) {
        Mono<Object> entity;
        if (rateLimiter.acquirePermission()) {
            entity = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("key", apiKey)
                            .queryParam("q", city)
                            .build())
                    .exchangeToMono(response -> {
                        if (response.statusCode().equals(HttpStatus.OK)) {
                            return response.bodyToMono(Object.class);
                        } else if (response.statusCode().is4xxClientError() || response.statusCode().is5xxServerError()) {
                            return response.bodyToMono(String.class)
                                    .map(jsonString -> {
                                            JsonObject jsonObject = (JsonObject) JsonParser.parseString(jsonString).getAsJsonObject().get("error");
                                            int errorCode = jsonObject.get("code").getAsInt();
                                            int errorStatus = WebClientErrorCodeFactory.getStatusByErrorCode(errorCode);
                                            return Mono.error(new WebClientExceptions(errorStatus, jsonObject.get("message").getAsString(), errorCode));
                                    });

                        } else {
                            return response.createException().flatMap(Mono::error);
                        }
                    });
        } else
            return Mono.error(WebClientExceptions.webClientErrorRequestLimitExceeded());
        return entity;
    }

    public  void deleteCityInformation (String city) {
        if(!mapIdReg.containsKey(city))
            throw ErrorResponse.notFoundCity();

        WeatherDao.weatherList.removeIf(weather -> weather.getReg().equals(city));
        mapIdReg.remove(city);

    }
}
