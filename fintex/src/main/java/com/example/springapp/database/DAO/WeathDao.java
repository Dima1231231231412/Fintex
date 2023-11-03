package com.example.springapp.database.DAO;

import com.example.springapp.database.entity.City;
import com.example.springapp.database.entity.Weather;
import com.example.springapp.database.entity.WeatherType;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;


public interface WeathDao {
    <S extends City> City save(S entity) throws SQLException;
    City findCityByName (String name) throws SQLException;
    void delete(City entity) throws SQLException;
    void updateCityName(Integer id,String name) throws SQLException;

    List<Weather> findWeathersByCityName(String cityName) throws SQLException;
    void delete(Weather entity) throws SQLException;
    <S extends Weather> S save(S entity) throws SQLException;
    Weather findWeatherByCityNameAndAndWeatherTypeNameAndDateTimeMeasurement (String cityName,String weatherType,Timestamp dateTimeMeasurement) throws SQLException;
    void updateWeather (Integer id, String cityName, Integer temperature, String weatherTypeName, Timestamp dateTimeMeasurement) throws SQLException;

    void delete(WeatherType entity) throws SQLException;
    <S extends WeatherType> S save(S entity) throws SQLException;
    WeatherType findWeatherTypeByName (String name) throws SQLException;
    void updateWeatherTypeName(Integer id,String name) throws SQLException;
}
