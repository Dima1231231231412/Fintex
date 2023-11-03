package com.example.springapp.database.JPA;


import com.example.springapp.database.entity.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface WeatherJpa extends JpaRepository<Weather,Integer> {


    List<Weather> findWeathersByCityName(String cityName);

    @Override
    void delete(Weather entity);

    @Modifying
    @Query("UPDATE Weather SET cityName =?2,temperature = ?3,weatherTypeName = ?4,dateTimeMeasurement = ?5 WHERE id = ?1")
    void updateWeather (Integer id, String cityName, Integer temperature, String weatherTypeName, Timestamp dateTimeMeasurement);

     @Override
    <S extends Weather> S save(S entity);
    Weather findWeatherByCityNameAndAndWeatherTypeNameAndDateTimeMeasurement(String cityName,String weatherType,Timestamp datetimeMeasurement);

}
