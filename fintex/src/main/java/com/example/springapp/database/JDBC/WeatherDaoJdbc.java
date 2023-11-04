package com.example.springapp.database.JDBC;

import com.example.springapp.database.DAO.WeathDao;
import com.example.springapp.database.entity.City;
import com.example.springapp.database.entity.Weather;
import com.example.springapp.database.entity.WeatherType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class WeatherDaoJdbc implements WeathDao {
    @Autowired
    private final DataSource dataSource;
    @Autowired
    private final TransactionTemplate transactionTemplate;

    @Autowired
    public WeatherDaoJdbc(DataSource dataSource, TransactionTemplate transactionTemplate) {
        this.dataSource = dataSource;
        this.transactionTemplate = transactionTemplate;
    }

    @Override
    public <S extends City> S save(S entity) throws SQLException {
        Connection connection = dataSource.getConnection();
        try (connection) {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO CITY (NAME) VALUES (?)");
            ps.setString(1, entity.getName());
            ps.executeUpdate();
            int cityId = findCityByName(entity.getName()).getId();
            entity.setId(cityId);
            return entity;
        }
        catch (SQLException e){
            e.printStackTrace();
            connection.rollback();
        }
        finally {
            connection.close();
        }
        return entity;
    }


    @Override
    public City findCityByName(String name) throws SQLException {
        Connection connection = dataSource.getConnection();
        City city = new City();
        try (connection) {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM CITY WHERE NAME=?");
            ps.setString(1, name);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                city.setId(resultSet.getInt(1));
                city.setName(resultSet.getString(2));
            }
            if (city.getId() == null) {
                city.setId(0);
            }
        }
        finally {
            connection.close();
        }
        return city;
    }

    @Override
    public void delete(City entity) throws SQLException {
        Connection connection = dataSource.getConnection();
        connection.setAutoCommit(false);
        try (connection){
            PreparedStatement ps = connection.prepareStatement("DELETE FROM CITY WHERE ID=?");
            ps.setInt(1,entity.getId());
            ps.executeUpdate();
            connection.commit();
        }
        catch (SQLException e){
            e.printStackTrace();
            connection.rollback();
        }
        finally {
            connection.close();
        }
    }

    @Override
    public void updateCityName(Integer id, String name) throws SQLException {
        Connection connection = dataSource.getConnection();
        connection.setAutoCommit(false);
        try (connection) {
            PreparedStatement ps = connection.prepareStatement("UPDATE CITY SET NAME = ? WHERE ID=?");
            ps.setString(1, name);
            ps.setInt(2, id);
            ps.executeUpdate();
            connection.commit();
        }
        catch (SQLException e){
            e.printStackTrace();
            connection.rollback();
        }
        finally {
            connection.close();
        }
    }

    //Получить все записи погоды по введённому городу
    @Override
    public List<Weather> findWeathersByCityName(String cityName) throws SQLException {
        Connection connection = dataSource.getConnection();
        List<Weather> weatherList = new ArrayList<>();
        try (connection) {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM WEATHER " +
                    "WHERE CITY_ID = (SELECT ID FROM CITY WHERE NAME=?)");
            ps.setString(1, cityName);
            ResultSet res = ps.executeQuery();
            while (res.next()) {
                weatherList.add(new Weather(res.getInt(1),
                        res.getString(2),
                        res.getInt(3),
                        res.getString(4),
                        res.getTimestamp(5))
                );
            }
        }
        finally {
            connection.close();
        }
        return weatherList;
    }

    @Override
    public void delete(Weather entity) throws SQLException {
        Connection connection = dataSource.getConnection();
        connection.setAutoCommit(false);
        try (connection){
            PreparedStatement ps = connection.prepareStatement("DELETE FROM WEATHER WHERE ID=?");
            ps.setInt(1,entity.getId());
            ps.executeUpdate();
            connection.commit();
        }
        catch (SQLException e){
            e.printStackTrace();
            connection.rollback();
        }
        finally {
            connection.close();
        }
    }


    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public <S extends Weather> S save(S entity) throws SQLException {
        Connection connection = dataSource.getConnection();
        connection.setAutoCommit(false);
        try(connection) {
            int cityId = findCityByName(entity.getCityName()).getId();
            if(cityId == 0){
                City city = new City();
                city.setName(entity.getCityName());
                cityId = save(city).getId();
            }

            int weatherTypeId = findWeatherTypeByName(entity.getWeatherTypeName()).getId();
            if(weatherTypeId == 0){
                WeatherType weatherType = new WeatherType();
                weatherType.setName(entity.getWeatherTypeName());
                weatherTypeId = save(weatherType).getId();
            }
            PreparedStatement ps = connection.prepareStatement("INSERT INTO WEATHER (CITY_ID, TEMPERATURE, WEATHER_TYPE_ID,DATETIME_MEASUREMENT) VALUES (?,?,?,?)");
            ps.setInt(1,cityId);
            ps.setInt(2, entity.getTemperature());
            ps.setInt(3,weatherTypeId);
            ps.setTimestamp(4, entity.getDateTimeMeasurement());
            ps.executeUpdate();
            connection.commit();
            entity.setId(findWeatherByCityNameAndAndWeatherTypeNameAndDateTimeMeasurement(
                    entity.getCityName(),
                    entity.getWeatherTypeName(),
                    entity.getDateTimeMeasurement()
            ).getId());
            return entity;
        }
        catch (SQLException e){
            e.printStackTrace();
            connection.rollback();
        }
        finally {
            connection.close();
        }
        return entity;
    }

    @Override
    public <S extends WeatherType> S save(S entity) throws SQLException {
        Connection connection = dataSource.getConnection();
        try (connection){
            PreparedStatement ps = connection.prepareStatement("INSERT INTO WEATHER_TYPE (NAME) VALUES (?)");
            ps.setString(1,entity.getName());
            ps.executeUpdate();
            WeatherType weatherType = findWeatherTypeByName(entity.getName());
            entity.setId(weatherType.getId());
            return entity;
        }
        catch (SQLException e){
            e.printStackTrace();
            connection.rollback();
        }
        finally {
            connection.close();
        }
        return entity;
    }

    @Override
    public WeatherType findWeatherTypeByName(String name) throws SQLException {
        Connection connection = dataSource.getConnection();
        try (connection){
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM WEATHER_TYPE WHERE NAME=?");
            ps.setString(1,name);
            ResultSet resultSet = ps.executeQuery();
            WeatherType weatherType = new WeatherType();
            while (resultSet.next()){
                weatherType.setId((byte) resultSet.getInt(1));
                weatherType.setName(resultSet.getString(2));
            }
            return weatherType;
        }
        finally {
            connection.close();
        }
    }

    @Override
    public void updateWeatherTypeName(Integer id, String name) throws SQLException {
        Connection connection = dataSource.getConnection();
        connection.setAutoCommit(false);
        try (connection) {
            PreparedStatement ps = connection.prepareStatement("UPDATE WEATHER_TYPE SET NAME =? WHERE id = ?");
            ps.setString(1, name);
            ps.setInt(2, id);
            ps.executeUpdate();
            connection.commit();
        }
        catch (SQLException e){
            e.printStackTrace();
            connection.rollback();
        }
        finally {
            connection.close();
        }
    }


    @Override
    public Weather findWeatherByCityNameAndAndWeatherTypeNameAndDateTimeMeasurement(String cityName, String weatherTypeName,Timestamp dateTimeMeasurement) throws SQLException {
        Connection connection = dataSource.getConnection();
        try (connection){
            int cityId = findCityByName(cityName).getId();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM WEATHER WHERE CITY_ID=? AND DATETIME_MEASUREMENT=?");
            ps.setInt(1,cityId);
            ps.setTimestamp(2,dateTimeMeasurement);

            ResultSet resultSet = ps.executeQuery();
            Weather weather = new Weather();
            while (resultSet.next()){
                weather.setId(resultSet.getInt(1));
                weather.setCityName(cityName);
                weather.setTemperature(resultSet.getInt(3));
                weather.setWeatherTypeName(weatherTypeName);
                weather.setDateTimeMeasurement(dateTimeMeasurement);
            }
            connection.close();
            return weather;
        }
        finally {
            connection.close();
        }
    }

    @Override
    public void updateWeather(Integer id, String cityName, Integer temperature, String weatherTypeName, Timestamp dateTimeMeasurement) throws SQLException {
        transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        Connection connection = dataSource.getConnection();
        transactionTemplate.execute(status -> {
            try {
                int cityId = findCityByName(cityName).getId();
                if (cityId == 0) {
                    City city = new City();
                    city.setName(cityName);
                    cityId = save(city).getId();
                }

                int weatherTypeId = findWeatherTypeByName(weatherTypeName).getId();
                if (weatherTypeId == 0) {
                    WeatherType weatherType = new WeatherType();
                    weatherType.setName(weatherTypeName);
                    weatherTypeId = save(weatherType).getId();
                }
                PreparedStatement ps = connection.prepareStatement("UPDATE Weather SET CITY_ID =?,temperature = ?,WEATHER_TYPE_ID = ?,DATETIME_MEASUREMENT = ? WHERE id = ?");
                ps.setInt(1, cityId);
                ps.setInt(2, temperature);
                ps.setInt(3, weatherTypeId);
                ps.setTimestamp(4, dateTimeMeasurement);
                ps.setInt(5, id);
                ps.executeUpdate();
                connection.commit();
            } catch (SQLException e) {
                status.setRollbackOnly();
                e.printStackTrace();
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return null;
        });
    }

    @Override
    public void delete(WeatherType entity) throws SQLException {
        Connection connection = dataSource.getConnection();
        connection.setAutoCommit(false);
        try (connection){
            PreparedStatement ps = connection.prepareStatement("DELETE FROM WEATHER_TYPE WHERE ID=?");
            ps.setInt(1,entity.getId());
            ps.executeUpdate();
            connection.commit();
        }
        catch (SQLException e){
            e.printStackTrace();
            connection.rollback();
        }
        finally {
            connection.close();
        }
    }

}

