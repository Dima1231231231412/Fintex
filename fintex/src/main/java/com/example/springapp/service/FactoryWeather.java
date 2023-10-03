package com.example.springapp.service;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class FactoryWeather
{
    //словарь для сопоставления региона с его идентификатором
    static Map<String, Integer> mapIdReg = new HashMap<>();

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    public int generateId (String reg){
        if (!mapIdReg.containsKey(reg)) {
            mapIdReg.put(reg, mapIdReg.size() + 1);
        }
        return mapIdReg.get(reg);
    }

    //создание объекта класса Weather и присвоение идентификатора
    public Weather createWeather(String reg, int temp, String dateTime) throws ParseException {
        return new Weather(generateId(reg),reg,temp, LocalDateTime.parse(dateTime,formatter));
    }

}