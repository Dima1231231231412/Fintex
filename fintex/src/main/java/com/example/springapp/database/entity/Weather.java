package com.example.springapp.database.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.sql.Timestamp;


@Data
@AllArgsConstructor
@Entity
@NoArgsConstructor
@Table (name = "WEATHER")
public class Weather {
    @Id
    private Integer id;


@Column(name = "city_id")
    private String cityName;

    @Column(name = "temperature")
    private Integer temperature;

@Column(name = "weather_type_id")
    private String weatherTypeName;

    @Column(name="datetime_measurement")
    private Timestamp dateTimeMeasurement;

}
