package com.example.springapp;

import com.example.springapp.service.FactoryWeather;
import com.example.springapp.service.Weather;
import com.example.springapp.service.WeatherService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.text.ParseException;
import java.util.*;

@SpringBootApplication
public class FintechSpringBootApplication {

	public static void main(String[] args) throws ParseException {
		SpringApplication.run(FintechSpringBootApplication.class, args);

		FactoryWeather factoryWeather = new FactoryWeather();

		Weather weather1 = factoryWeather.createWeather("Vladimir", 21, "2023-10-01 09:30");
		Weather weather2 = factoryWeather.createWeather("Vladimir", 23, "2023-09-27 12:00");
		Weather weather3 = factoryWeather.createWeather("Vladimir", 24, "2023-09-13 13:30");
		Weather weather4 = factoryWeather.createWeather("Ryazan", 23, "2023-09-14 15:00");
		Weather weather5 = factoryWeather.createWeather("Ryazan", 22, "2023-09-14 16:30");
		Weather weather6 = factoryWeather.createWeather("Ryazan", 26, "2023-09-14 13:00");
		Weather weather7 = factoryWeather.createWeather("Moscow", 25, "2023-09-11 12:30");
		Weather weather8 = factoryWeather.createWeather("Moscow", 23, "2023-09-11 15:30");
		Weather weather9 = factoryWeather.createWeather("Moscow", 24, "2023-09-11 16:00");

		// список объектов

		WeatherService.listWeathers = new ArrayList<>(
				Arrays.asList(weather1, weather2, weather3, weather4, weather5, weather6, weather7, weather8, weather9)
		);

	}



}
