package com.example.springapp.scriptsSql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class TablesScriptSql {
    @Autowired
    @Qualifier("jdbcTemplateTest")
    JdbcTemplate jdbcTemplate;

    public void createTableCity(){
        jdbcTemplate.execute("""
                CREATE TABLE City (
                id int primary key auto_increment,
                name varchar(255) NOT NULL)
                """);

        jdbcTemplate.update("""
                    INSERT INTO City (name)
                    VALUES ('Ryazan');
                    INSERT INTO City (name)
                    VALUES ('Moscow');
                    INSERT INTO City (name)
                    VALUES ('Tomsk');
                    INSERT INTO City (name)
                    VALUES ('Omsk');
                """);
    }

    public void createTableWeatherType(){
        jdbcTemplate.execute("""
                CREATE TABLE Weather_Type (
                id int primary key auto_increment,
                name varchar(255) NOT NULL)
                """);
        jdbcTemplate.update("""
                INSERT INTO Weather_type (name)
                VALUES ('Снег');
                INSERT INTO Weather_type (name)
                VALUES ('Дождь');
                INSERT INTO Weather_type (name)
                VALUES ('Град');
                INSERT INTO Weather_type (name)
                VALUES ('Небольшой дождь');
                INSERT INTO Weather_type (name)
                VALUES ('Ливень');
                INSERT INTO Weather_type (name)
                VALUES ('Пасмурно');
                INSERT INTO Weather_type (name)
                VALUES ('Ясно');
                INSERT INTO Weather_type (name)
                VALUES ('Малооблачно');
            """);
    }

    public void createTableWeather(){
        //Сразу создаём остальные таблицы, чтобы не было конфликта FK
        createTableCity();
        createTableWeatherType();

        jdbcTemplate.execute("""
                CREATE TABLE Weather (
                    id int primary key auto_increment,
                    city_id int NOT NULL,
                    temperature int NOT NULL,
                    weather_type_id int NOT NULL,
                    datetime_measurement timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (city_id) REFERENCES city(id) ON UPDATE CASCADE ON DELETE RESTRICT,
                    FOREIGN KEY (weather_type_id) REFERENCES Weather_type(id) ON UPDATE CASCADE ON DELETE RESTRICT
                );
                """);
        jdbcTemplate.update("""
                   INSERT INTO weather (city_id,temperature,weather_type_id)
                   VALUES (1,10,2);
                   INSERT INTO weather (city_id,temperature,weather_type_id)
                   VALUES (2,13,2);
                   INSERT INTO weather (city_id,temperature,weather_type_id)
                   VALUES (3,16,1);
                   INSERT INTO weather (city_id,temperature,weather_type_id,datetime_measurement)
                   VALUES (4,10,5,'2023-10-28 17:15');
                   INSERT INTO weather (city_id,temperature,weather_type_id, datetime_measurement)
                   VALUES (1,17,7,'2023-10-13 13:00');
                   INSERT INTO weather (city_id,temperature,weather_type_id, datetime_measurement)
                   VALUES (2,15,8,'2023-10-13 15:00');
                   INSERT INTO weather (city_id,temperature,weather_type_id, datetime_measurement)
                   VALUES (3,9,8,'2023-10-13 18:00');
               """);
    }
}
