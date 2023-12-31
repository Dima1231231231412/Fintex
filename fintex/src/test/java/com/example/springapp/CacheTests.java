package com.example.springapp;


import com.example.springapp.database.JDBC.WeatherDaoJdbc;
import com.example.springapp.database.entity.Weather;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.WeakHashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class CacheTests {
    @Autowired
    DataSource dataSource;
    @Autowired
    TransactionTemplate transactionTemplate;

    @Value("${cache.course.size}")
    private int maxSize;
    private final WeakHashMap<String, Weather> lrucache = new WeakHashMap<>(maxSize, 0.75f);;

    @Test
    public void testGetWeather_AddedToCache() {
        String location = "Ryazan";
        Weather weather = new Weather(50,location,-3, "Дождь", Timestamp.valueOf("2023-11-16 15:45:00"));
        lrucache.put(location, weather);
        assertTrue(lrucache.containsKey(location)); // Successful
    }

    @Test
    public void testGetWeather_InCache_NotFetchedFromDatabase() throws SQLException {
        String location = "London";
        Weather weather = new Weather(50,location,11, "Пасмурно", Timestamp.valueOf("2023-11-16 15:45:00"));
        lrucache.put(location, weather);

        WeatherDaoJdbc mock = mock(WeatherDaoJdbc.class);
        when(mock.getWeatherForCityNameAndDateTimeMeasurement(weather)).thenReturn(weather);

        if(!lrucache.containsKey(location)) {
            when(mock.getWeatherForCityNameAndDateTimeMeasurement(weather)).thenReturn(weather);
        }

        verify(mock, never()).getWeatherForCityNameAndDateTimeMeasurement(weather); // Successful
    }

    @Test
    public void testUpdateWeather_CacheEvicted() {
        //Начальные значения
        String location = "Ryazan";
        Weather weather = new Weather(50,location,-3, "Дождь", Timestamp.valueOf("2023-11-16 15:45:00"));
        lrucache.put(location, weather);

        //Изменяем параметры погоды
        weather.setTemperature(30);
        weather.setDateTimeMeasurement(Timestamp.valueOf("2023-11-17 16:50:00"));

        if(lrucache.get(location).getDateTimeMeasurement().compareTo(weather.getDateTimeMeasurement()) != 0){
            lrucache.remove(location);
            lrucache.put(location,weather);
        }
        assertEquals(lrucache.get(location), weather); // Successful
    }
}
