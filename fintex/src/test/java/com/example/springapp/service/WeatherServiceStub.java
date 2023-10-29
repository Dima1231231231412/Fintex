package com.example.springapp.service;
import java.text.ParseException;

public class WeatherServiceStub extends WeatherService {

    @Override
    public com.example.springapp.service.Weather updateTemperatureInCity(String city, com.example.springapp.service.Weather weather) throws ParseException {
        int index = Integer.MAX_VALUE;
        for (int i = 0; i < WeatherDao.weatherList.size(); i++) {
            if (WeatherDao.weatherList.get(i).getReg().equals(city) && WeatherDao.weatherList.get(i).getDateTime().compareTo(weather.getDateTime()) == 0)
                index = i;
        }

        if (index == Integer.MAX_VALUE) {
            FactoryWeather fw = new FactoryWeather();
            Weather newWeather = fw.createWeather(city, weather.getTemp(), weather.getDateTime().toString());
            WeatherDao.weatherList.add(newWeather);
            return newWeather;
        }
        WeatherDao.weatherList.get(index).setTemp(weather.getTemp());
        return WeatherDao.weatherList.get(index);
    }
}
