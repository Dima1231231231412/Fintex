package com.example.springapp;

import com.example.springapp.service.CurrentWeatherDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@Component
@NoArgsConstructor
public class WebClientApiWeatherDTO {
    private String city;
    private float temperature;
    private String weatherType;
    private Timestamp dateTimeMeasurement;


    public WebClientApiWeatherDTO of (CurrentWeatherDTO currentWeather){
        return new WebClientApiWeatherDTO(
                currentWeather.getLocation().getName(),
                currentWeather.getCurrent().getTemp_c(),
                currentWeather.getCurrent().getCondition().getText(),
                Timestamp.valueOf(currentWeather.getLocation().getLocaltime()+":00")
        );
    }
}
