package com.example.springapp.database.DAO;

import com.example.springapp.database.JPA.CityJpa;
import com.example.springapp.database.JPA.WeatherJpa;
import com.example.springapp.database.JPA.WeatherTypeJpa;
import com.example.springapp.database.entity.City;
import com.example.springapp.database.entity.Weather;
import com.example.springapp.database.entity.WeatherType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

@AllArgsConstructor
@Component
public class WeatherDaoHibernate implements  WeathDao{
    private final WeatherJpa weatherJpa;
    private final WeatherTypeJpa weatherTypeJpa;
    private final CityJpa cityJpa;


    @Override
    public <S extends City> S save(S entity) {
        return cityJpa.save(entity);
    }

    @Override
    public <S extends Weather> S save(S entity) {
        return weatherJpa.save(entity);
    }

    @Override
    public <S extends WeatherType> S save(S entity) {
        return weatherTypeJpa.save(entity);
    }

    @Override
    public WeatherType findWeatherTypeByName(String name) {
        return weatherTypeJpa.findWeatherTypeByName(name);
    }

    @Override
    public void updateWeatherTypeName(Integer id, String name) {
        weatherTypeJpa.updateWeatherTypeName(id,name);
    }

    @Override
    public Weather findWeatherByCityNameAndAndWeatherTypeNameAndDateTimeMeasurement(String cityName, String weatherType,Timestamp dateTimeMeasurement) {
        return weatherJpa.findWeatherByCityNameAndAndWeatherTypeNameAndDateTimeMeasurement (cityName,weatherType,dateTimeMeasurement);
    }

    @Override
    public void updateWeather(Integer id, String cityName, Integer temperature, String weatherTypeName, Timestamp dateTimeMeasurement) {
        weatherJpa.updateWeather(id,cityName,temperature,weatherTypeName,dateTimeMeasurement);
    }

    @Override
    public void delete(WeatherType entity) {
        weatherTypeJpa.delete(entity);
    }

    @Override
    public City findCityByName(String name) {
        return cityJpa.findCityByName(name);
    }

    @Override
    public void delete(City entity) {
        cityJpa.delete(entity);
    }

    @Override
    public void updateCityName(Integer id, String name) {
        cityJpa.updateCityName(id,name);
    }

    @Override
    public List<Weather> findWeathersByCityName(String cityName) {
        return weatherJpa.findWeathersByCityName(cityName);
    }

    @Override
    public void delete(Weather entity) {
        weatherJpa.delete(entity);
    }
}
