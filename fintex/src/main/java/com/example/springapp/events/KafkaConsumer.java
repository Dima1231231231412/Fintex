package com.example.springapp.events;

import com.example.springapp.database.JDBC.WeatherDaoJdbc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaConsumer {
    @Autowired
    WeatherDaoJdbc weatherDaoJdbc;

    @KafkaListener(topics = "weather-data", groupId = "weather-group")
    public void flightWeatherDataEventConsumer(@Payload KafkaProducer.MyEvent myEvent) {
        try {
            //Выводим среднее скользящее за 30 периодов по городу
            String city = myEvent.getKey();
            log.info("City: {}, 30 period moving average temperature: {}",
                    city,
                    weatherDaoJdbc.getAvgTemperatureInCityIn30records(city)
            );
        }
        catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}

