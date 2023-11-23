package com.example.springapp.cache;

import com.example.springapp.database.entity.Weather;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class CacheComponents {
    @Bean
    public Cache<Weather> cacheWeather (){
        return new Cache<>();
    }
}
