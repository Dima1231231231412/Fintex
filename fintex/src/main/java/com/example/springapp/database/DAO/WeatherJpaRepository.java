package com.example.springapp.database.DAO;

import com.example.springapp.database.entity.City;
import com.example.springapp.database.entity.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@EnableJpaRepositories
@Repository
public interface WeatherJpaRepository extends JpaRepository<Weather,Integer> {
    String queryUpdate = "UPDATE WEATHER SET CITY_ID=?,TEMPERATURE = ?,WEATHER_TYPE_ID = ?,DATETIME_MEASUREMENT = ? WHERE ID = ?";


    List<Weather> findWeathersByCityName(String cityName);
    Weather deleteWeathersByCityNameAndDateTimeMeasurement(String cityName,Timestamp datetimeMeasurement);


    @Modifying
    @Query(value = queryUpdate)
    int updateWeather (Integer weather_id, String city, Integer temperature, String weather_type, Timestamp dateTime_measurement);


    <S extends Weather> S save(S entity);
}
