package com.example.springapp.events;

import com.example.springapp.service.WeatherApiClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.sql.SQLException;

@Slf4j
@Configuration
@EnableScheduling
public class Events {
    @Autowired
    WeatherApiClientService weatherApiClientService;
    @Autowired
    KafkaProducer kafkaProducer;

    private static final String[] locations = {
            "Moscow",
            "Ryazan",
            "Samara",
            "Kazan",
            "Novosibirsk"
    };
    private int cityIndex = 0;

    //Каждую минуту получаем погоду по городу
    @Scheduled(cron = "0 * * * * *")
    public void scheduleFixedRateTask() throws SQLException{
        String location = locations[cityIndex];
        Object weatherData = weatherApiClientService.getCurrentWeatherCache(location);
        KafkaProducer.MyEvent myEvent = new KafkaProducer.MyEvent(location,weatherData);

        kafkaProducer.sendWeatherDataEvent(myEvent,"weather-data");
        cityIndex += 1;
        if((cityIndex)/locations.length == 1)
            cityIndex = 0;
    }
}
