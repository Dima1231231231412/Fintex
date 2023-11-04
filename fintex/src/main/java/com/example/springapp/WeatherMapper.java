package com.example.springapp;

import com.example.springapp.database.entity.Weather;
import com.example.springapp.service.CurrentWeatherDTO;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class WeatherMapper {

    public Weather of (CurrentWeatherDTO currentWeather){
        return new Weather(
                0,
                currentWeather.getLocation().getName(),
                (int) currentWeather.getCurrent().getTemp_c(),
                currentWeather.getCurrent().getCondition().getText(),
                Timestamp.valueOf(currentWeather.getLocation().getLocaltime()+":00")
        );
    }
}

