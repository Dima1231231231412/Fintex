package com.example.springapp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Weather {
    private int id;
    private int cityId;
    private int temperature;
    private int weatherTypeId;
    private Timestamp dateTimeMeasurement;
}
