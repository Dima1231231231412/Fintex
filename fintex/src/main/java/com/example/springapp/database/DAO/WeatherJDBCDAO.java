package com.example.springapp.database.DAO;
import com.example.springapp.database.entity.Weather;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class WeatherJDBCDAO {
    private final DataSource dataSource;

    public WeatherJDBCDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public int findCityIdByName(String cityNameIn) throws SQLException {
        PreparedStatement ps = dataSource.getConnection().prepareStatement("SELECT ID FROM CITY WHERE NAME=?");
        ps.setString(1,cityNameIn);
        ResultSet resultSet = ps.executeQuery();
        int city_id = 0;
        while (resultSet.next()){
            city_id = resultSet.getInt(1);
        }
        return city_id;
    }

    public boolean createCity(String name) throws SQLException {
        PreparedStatement ps = dataSource.getConnection().prepareStatement("INSERT INTO CITY (NAME) VALUES (?)");
        ps.setString(1,name);
        return ps.execute();
    }

    public short findWeatherTypeIdByName(String weatherTypeNameIn) throws SQLException {
        PreparedStatement ps = dataSource.getConnection().prepareStatement("SELECT ID FROM WEATHER_TYPE WHERE NAME=?");
        ps.setString(1,weatherTypeNameIn);
        ResultSet resultSet = ps.executeQuery();
        short weather_type_id = 0;
        while (resultSet.next()){
            weather_type_id = resultSet.getShort(1);
        }
        return weather_type_id;
    }

    public boolean createWeatherType(String weatherTypeName) throws SQLException {
        PreparedStatement ps = dataSource.getConnection().prepareStatement("INSERT INTO WEATHER_TYPE (NAME) VALUES (?)");
        ps.setString(1,weatherTypeName);
        return ps.execute();
    }

    //Создать запись с погодой
    //если название города или типа погоды отсутствуют в справочниках - создаст записи в справочниках
    public boolean createWeather(Weather weather) throws SQLException {
        int city_id = findCityIdByName(weather.getCityName());
        if(city_id == 0){
            boolean a = createCity(weather.getCityName());
            if(a)
                city_id = findCityIdByName(weather.getCityName());
        }

        int weather_type_id = findWeatherTypeIdByName(weather.getWeatherTypeName());
        if(weather_type_id == 0){
            boolean a = createWeatherType(weather.getWeatherTypeName());
            if(a)
                weather_type_id = findWeatherTypeIdByName(weather.getWeatherTypeName());
        }

        PreparedStatement ps = dataSource.getConnection().prepareStatement("INSERT INTO WEATHER (CITY_ID, TEMPERATURE, WEATHER_TYPE_ID,DATETIME_MEASUREMENT) VALUES (?,?,?,?)");
        ps.setInt(1,city_id);
        ps.setInt(2, weather.getTemperature());
        ps.setInt(3,weather_type_id);
        ps.setTimestamp(4, weather.getDateTimeMeasurement());
        return ps.execute();
    }

    //Получить все записи погоды по введённому городу
    public List<Weather> getAllRecordsWeatherInCity(String city_name) throws SQLException {
        PreparedStatement ps = dataSource.getConnection().prepareStatement("SELECT ID, CITY_ID, TEMPERATURE, WEATHER_TYPE_ID, DATETIME_MEASUREMENT FROM WEATHER " +
                                                                "WHERE CITY_ID = (SELECT ID FROM CITY WHERE NAME=?)");
        ps.setString(1,city_name);
        ResultSet res = ps.executeQuery();
        List<Weather> weatherList = new ArrayList<>();
        while (res.next()){
            weatherList.add(new Weather(res.getInt(1),
                    res.getString(2),
                    res.getInt(3),
                    res.getString(4),
                    res.getTimestamp(5))
            );
        }
        return weatherList;
    }

    //Удаление записи с погодой по введённому городу и дате со временем
    public boolean deleteWeather(String city,Timestamp dateTime_measurement) throws SQLException {
        PreparedStatement ps = dataSource.getConnection().prepareStatement("DELETE FROM WEATHER WHERE CITY_ID = (SELECT ID FROM CITY WHERE NAME = ?) AND DATETIME_MEASUREMENT=?");
        ps.setString(1,city);
        ps.setTimestamp(2,dateTime_measurement);
        return ps.execute();
    }

    //Обновление погоды по текущему городу
    public boolean updateWeather(Integer weather_id, String city, Integer temperature, String weather_type, Timestamp dateTime_measurement) throws SQLException {
        int city_id = findCityIdByName(city);
        if(city_id == 0){
            return false;
        }
        System.out.println(city_id);

        short weather_type_id = findWeatherTypeIdByName(weather_type);
        if(weather_type_id == 0){
            boolean resCreate = createWeatherType(weather_type);
            if (resCreate){
                weather_type_id = findWeatherTypeIdByName(weather_type);
            }
        }
        System.out.println(weather_type_id);
        PreparedStatement ps = dataSource.getConnection().prepareStatement("UPDATE WEATHER SET CITY_ID=?," +
                                                                                    " TEMPERATURE = ?," +
                                                                                    " WEATHER_TYPE_ID = ?," +
                                                                                    " DATETIME_MEASUREMENT = ?" +
                                                                "WHERE ID = ?");
        ps.setInt(1,city_id);
        ps.setInt(2,temperature);
        ps.setShort(3,weather_type_id);
        ps.setTimestamp(4,dateTime_measurement);
        ps.setInt(5,weather_id);
        return ps.execute();
    }


}
