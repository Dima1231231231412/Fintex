package com.example.springapp.database.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@Data
@AllArgsConstructor
@Entity
@NoArgsConstructor
@Table (name = "weather_type")
public class WeatherType {
    @Id
    private byte id;

    @Column(name = "name")
    private String name;

}
