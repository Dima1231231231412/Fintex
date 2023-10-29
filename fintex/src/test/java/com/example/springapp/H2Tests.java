package com.example.springapp;

import com.example.springapp.entity.Weather;
import com.example.springapp.scriptsSql.TablesScriptSql;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class H2Tests {
    @Autowired
    @Qualifier("jdbcTemplateTest")
    JdbcTemplate jdbcTemplate;

    @Autowired
    TablesScriptSql tablesScriptSql;


    @Test
    public void testFindCityIdByNameFromTableCity() {
        tablesScriptSql.createTableCity();
        Integer testId = jdbcTemplate.queryForObject("SELECT ID FROM CITY WHERE NAME='Moscow'", Integer.class);
        assertThat(testId).isEqualTo(2);    // Successful
    }

    @Test
    public void testCreateCity() {
        tablesScriptSql.createTableCity();
        int testId = jdbcTemplate.update("INSERT INTO CITY (NAME) VALUES ('testCity')");
        assertThat(testId).isZero();   // Fatal
    }


    @Test
    public void testFindWeatherTypeIdByNameFromTableWeatherType() {
        tablesScriptSql.createTableWeatherType();
        Integer testId = jdbcTemplate.queryForObject("SELECT ID FROM WEATHER_TYPE WHERE NAME='Ливень'", Integer.class);
        assertThat(testId).isEqualTo(5);   // Successful
    }

    @Test
    public void testCreateWeatherType() {
        tablesScriptSql.createTableWeatherType();
        int testId = jdbcTemplate.update("INSERT INTO WEATHER_TYPE (NAME) VALUES (3)");
        assertThat(testId).isZero();   // Successful
    }

    @Test
    public void testFindWeathersByCityName() {
        tablesScriptSql.createTableWeather();

        // Тестовые списки
        List<Weather> weatherList1 = new ArrayList<>(List.of(
                new Weather(4, 4, 10, 5, Timestamp.valueOf("2023-10-28 17:15:00"))
        ));
        List<Weather> weatherList2 = new ArrayList<>(List.of(
                new Weather(5, 4, 7, 5, Timestamp.valueOf("2023-10-24 14:15:00"))
        ));
        List<Weather> weatherList3 = new ArrayList<>(List.of(
                new Weather(5, 6, 4, 5, Timestamp.valueOf("2023-09-04 05:15:00")),
                new Weather(5, 4, 7, 5, Timestamp.valueOf("2023-10-28 21:45:00"))
        ));

        List<Weather> weatherTest = jdbcTemplate.query(
                "SELECT ID, CITY_ID, TEMPERATURE, WEATHER_TYPE_ID, DATETIME_MEASUREMENT " +
                    "FROM WEATHER " +
                    "WHERE CITY_ID = (SELECT ID FROM CITY WHERE NAME='Omsk')",
                (resultSet, rowNum) ->  new Weather(
                            resultSet.getInt(1),
                            resultSet.getInt(2),
                            resultSet.getInt(3),
                            resultSet.getInt(4),
                            resultSet.getTimestamp(5)
                    )
                );
        assertAll(
                "Grouped Assertions of Weather",
                () -> assertThat(weatherTest.equals(weatherList1)).isTrue(), // Successful
                () -> assertThat(weatherTest.equals(weatherList2)).isTrue(), // Fatal
                () -> assertThat(weatherTest.equals(weatherList3)).isTrue()  // Fatal
        );
    }

    @Test
    public void testDeleteWeathersByCityNameAndDateTimeMeasurement() {
        tablesScriptSql.createTableWeather();
        int testId = jdbcTemplate.update("DELETE FROM WEATHER WHERE CITY_ID = (SELECT ID FROM CITY WHERE NAME = 'Tomsk') AND DATETIME_MEASUREMENT='2023-10-13 18:00'");
        assertThat(testId).isOne();   // Successful
    }

    @Test
    public void testCreateWeather() throws SQLException {
        tablesScriptSql.createTableWeather();
        // Тестируемые данные
        WebClientApiWeatherDTO webClientApiWeatherDTO = new WebClientApiWeatherDTO(
                "Rostov",-5f,"Ветер с дождём",Timestamp.valueOf("2023-10-28 21:45:00")
        );

        // Если город/тип погоды не существует - заносим данные в соответствующие таблицы
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

        // Добавление погоды в БД
        createWeather(city_id,(int) webClientApiWeatherDTO.getTemperature(),weather_type_id,webClientApiWeatherDTO.getDateTimeMeasurement());

        // Десериализация добавленных данных в Weather
        Weather addedWeather = jdbcTemplate.queryForObject("SELECT * FROM WEATHER WHERE CITY_ID=? AND DATETIME_MEASUREMENT=?",
                (resultSet, rowNum) ->  new Weather(
                        resultSet.getInt(1),
                        resultSet.getInt(2),
                        resultSet.getInt(3),
                        resultSet.getInt(4),
                        resultSet.getTimestamp(5)
                ),
                city_id,webClientApiWeatherDTO.getDateTimeMeasurement());

        // Получаем название города и типы погоды по идентификаторам из Weather
        String cityName = jdbcTemplate.queryForObject("SELECT NAME FROM CITY WHERE ID=?",String.class,addedWeather.getCityId());
        String weatherName = jdbcTemplate.queryForObject("SELECT NAME FROM WEATHER_TYPE WHERE ID=?",String.class,addedWeather.getWeatherTypeId());

        assertAll(
                "Grouped Assertions of Weather",
                () -> assertThat(webClientApiWeatherDTO.getCity()).isEqualTo(cityName), // Successful
                () -> assertThat(webClientApiWeatherDTO.getTemperature()).isEqualTo(addedWeather.getTemperature()+1), // Fatal
                () -> assertThat(webClientApiWeatherDTO.getDateTimeMeasurement()).isEqualTo(addedWeather.getDateTimeMeasurement()), // Successful
                () -> assertThat(webClientApiWeatherDTO.getWeatherType()).isEqualTo(weatherName) // Successful
        );
    }

    public int findCityIdByName(String cityName) {
        try {
            return jdbcTemplate.queryForObject("SELECT ID FROM CITY WHERE NAME=?",Integer.class,cityName);
        }
        // Поскольку метод queryForObject ожидает, что запрос всегда должен возвращать хотя бы одну строку
        // перехватываем исключение и возвращаем результат - 0
        catch (EmptyResultDataAccessException e) {
            return 0;
        }
    }

    public boolean createCity(String cityName) {
        int res = jdbcTemplate.update("INSERT INTO CITY (NAME) VALUES (?)", cityName);
        return res == 1;
    }
    public int findWeatherTypeIdByName(String weatherName) {
        try {
            return jdbcTemplate.queryForObject("SELECT ID FROM WEATHER_TYPE WHERE NAME=?", Integer.class, weatherName);
        }
        //
        catch (EmptyResultDataAccessException e) {
            return 0;
        }
    }
    public boolean createWeatherType (String weatherTypeName) {
        int res = jdbcTemplate.update("INSERT INTO WEATHER_TYPE (NAME) VALUES (?)",weatherTypeName);
        return res == 1;
    }

    public int createWeather(int cityId, int temperature, int weatherTypeId, Timestamp dateTimeMeasurement) {
        return jdbcTemplate.update("INSERT INTO WEATHER (CITY_ID, TEMPERATURE, WEATHER_TYPE_ID,DATETIME_MEASUREMENT) VALUES (?,?,?,?)",cityId,temperature,weatherTypeId,dateTimeMeasurement);
    }
}
