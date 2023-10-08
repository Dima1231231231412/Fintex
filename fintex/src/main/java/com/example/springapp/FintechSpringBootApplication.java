package com.example.springapp;

import com.example.springapp.service.FactoryWeather;
import com.example.springapp.service.WeatherService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class FintechSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(FintechSpringBootApplication.class, args);
	}
}
