package com.example.springapp.database.DAO;
import com.example.springapp.WebClientApiWeatherDTO;
import com.example.springapp.database.entity.Weather;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class WeatherJdbcDAO {
    @Autowired
    private final Connection connection;
    @Autowired
    private final TransactionTemplate transactionTemplate;

    public WeatherJdbcDAO(Connection connection, TransactionTemplate transactionTemplate) {
        this.connection = connection;
        this.transactionTemplate = transactionTemplate;
    }

    public boolean createWeather(WebClientApiWeatherDTO webClientApiWeatherDTO) throws SQLException {
        try (connection) {
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            int city_id = findCityIdByName(webClientApiWeatherDTO.getCity());
            if(city_id == 0){
                boolean a = createCity(webClientApiWeatherDTO.getCity());
                if(a)
                    city_id = findCityIdByName(webClientApiWeatherDTO.getCity());
            }

            int weather_type_id = findWeatherTypeIdByName(webClientApiWeatherDTO.getWeatherType());
            if(weather_type_id == 0){
                boolean a = createWeatherType(webClientApiWeatherDTO.getWeatherType());
                if(a)
                    weather_type_id = findWeatherTypeIdByName(webClientApiWeatherDTO.getWeatherType());
            }

            PreparedStatement ps = connection.prepareStatement("INSERT INTO WEATHER (CITY_ID, TEMPERATURE, WEATHER_TYPE_ID,DATETIME_MEASUREMENT) VALUES (?,?,?,?)");
            ps.setInt(1,city_id);
            ps.setInt(2, (int) webClientApiWeatherDTO.getTemperature());
            ps.setInt(3,weather_type_id);
            ps.setTimestamp(4, webClientApiWeatherDTO.getDateTimeMeasurement());
            ps.executeUpdate();
            connection.commit();
            return true;
        } catch (SQLException ex) {
            connection.rollback();
            ex.printStackTrace();
            return false;
        }
    }

    public int findCityIdByName(String cityNameIn) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT ID FROM CITY WHERE NAME=?");
        ps.setString(1,cityNameIn);
        ResultSet resultSet = ps.executeQuery();
        int city_id = 0;
        while (resultSet.next()){
            city_id = resultSet.getInt(1);
        }
        return city_id;
    }

    public boolean createCity(String cityName) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("INSERT INTO CITY (NAME) VALUES (?)");
        ps.setString(1,cityName);
        return ps.executeUpdate() == 1;
    }

    public short findWeatherTypeIdByName(String weatherTypeNameIn) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT ID FROM WEATHER_TYPE WHERE NAME=?");
        ps.setString(1,weatherTypeNameIn);
        ResultSet resultSet = ps.executeQuery();
        short weather_type_id = 0;
        while (resultSet.next()){
            weather_type_id = resultSet.getShort(1);
        }
        return weather_type_id;
    }

    public boolean createWeatherType(String weatherTypeName) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("INSERT INTO WEATHER_TYPE (NAME) VALUES (?)");
        ps.setString(1,weatherTypeName);
        return ps.executeUpdate() == 1;
    }


    //Создать запись с погодой
    //если название города или типа погоды отсутствуют в справочниках - создаст записи в справочниках
    @Transactional(isolation = Isolation.SERIALIZABLE)
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

        PreparedStatement ps = connection.prepareStatement("INSERT INTO WEATHER (CITY_ID, TEMPERATURE, WEATHER_TYPE_ID,DATETIME_MEASUREMENT) VALUES (?,?,?,?)");
        ps.setInt(1,city_id);
        ps.setInt(2, weather.getTemperature());
        ps.setInt(3,weather_type_id);
        ps.setTimestamp(4, weather.getDateTimeMeasurement());
        return ps.executeUpdate() == 1;
    }

    //Получить все записи погоды по введённому городу
    public List<Weather> getAllRecordsWeatherInCity(String cityName) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT ID, CITY_ID, TEMPERATURE, WEATHER_TYPE_ID, DATETIME_MEASUREMENT FROM WEATHER " +
                                                                "WHERE CITY_ID = (SELECT ID FROM CITY WHERE NAME=?)");
        ps.setString(1,cityName);
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
    public boolean deleteWeather(String city,Timestamp dateTimeMeasurement) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("DELETE FROM WEATHER WHERE CITY_ID = (SELECT ID FROM CITY WHERE NAME = ?) AND DATETIME_MEASUREMENT=?");
        ps.setString(1,city);
        ps.setTimestamp(2,dateTimeMeasurement);
        return ps.executeUpdate() == 1;
    }

    //Обновление погоды по текущему городу
    public boolean updateWeather(Integer weatherId, String city, Integer temperature, String weatherType, Timestamp dateTimeMeasurement) {
        transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        transactionTemplate.execute(status -> {
            try{
                int city_id = findCityIdByName(city);
                if(city_id == 0){
                    return false;
                }

                short weather_type_id = findWeatherTypeIdByName(weatherType);
                if(weather_type_id == 0){
                    boolean resCreate = createWeatherType(weatherType);
                    if (resCreate){
                        weather_type_id = findWeatherTypeIdByName(weatherType);
                    }
                }
                PreparedStatement ps = connection.prepareStatement("UPDATE WEATHER SET CITY_ID=?," +
                        " TEMPERATURE = ?," +
                        " WEATHER_TYPE_ID = ?," +
                        " DATETIME_MEASUREMENT = ?" +
                        "WHERE ID = ?");
                ps.setInt(1,city_id);
                ps.setInt(2,temperature);
                ps.setShort(3, weather_type_id);
                ps.setTimestamp(4,dateTimeMeasurement);
                ps.setInt(5,weatherId);
                boolean res = ps.executeUpdate() == 1;
                connection.commit();
                return res;
            }
            catch (SQLException e){
                status.setRollbackOnly();
                e.printStackTrace();
                return false;
            }
        });
        return true;
    }




}
