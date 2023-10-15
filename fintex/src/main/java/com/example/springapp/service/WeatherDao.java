package com.example.springapp.service;


import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class WeatherDao {
    public static List<Weather> weatherList;

    //инициализация данных
    @PostConstruct
    public List<Weather> init() {
            FactoryWeather factoryWeather = new FactoryWeather();
            try {
                weatherList = new ArrayList<>(List.of(
                        factoryWeather.createWeather("Vladimir", 21, "2023-10-08T09:30"),
                        factoryWeather.createWeather("Vladimir", 23, "2023-09-27T12:00"),
                        factoryWeather.createWeather("Vladimir", 24, "2023-09-13T13:30"),
                        factoryWeather.createWeather("Ryazan", 23, "2023-09-14T15:00"),
                        factoryWeather.createWeather("Ryazan", 22, "2023-09-14T16:30"),
                        factoryWeather.createWeather("Ryazan", 26, "2023-09-14T13:00"),
                        factoryWeather.createWeather("Moscow", 25, "2023-09-11T12:30"),
                        factoryWeather.createWeather("Moscow", 23, "2023-09-11T15:30"),
                        factoryWeather.createWeather("Moscow", 24, "2023-09-11T16:00")
                ));
            } catch (Exception e) {
                log.error("Произошла ошибка при инициализации списка с погодой", e);
                System.exit(-1);
            }
            return weatherList;
    }
}
