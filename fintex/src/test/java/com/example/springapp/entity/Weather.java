package com.example.springapp.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
public class Weather {
    private int id;
    private int cityId;
    private int temperature;
    private int weatherTypeId;
    private Timestamp dateTimeMeasurement;

    public Weather(int id, int cityId, int temperature, int weatherTypeId, Timestamp dateTimeMeasurement) {
        this.id = id;
        this.cityId = cityId;
        this.temperature = temperature;
        this.weatherTypeId = weatherTypeId;
        this.dateTimeMeasurement = dateTimeMeasurement;
    }

    public int getId() {
        return id;
    }

    public int getCityId() {
        return cityId;
    }

    public int getTemperature() {
        return temperature;
    }

    public int getWeatherTypeId() {
        return weatherTypeId;
    }

    public Timestamp getDateTimeMeasurement() {
        return dateTimeMeasurement;
    }
}
