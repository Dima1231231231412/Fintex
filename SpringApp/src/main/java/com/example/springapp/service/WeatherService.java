package com.example.springapp.service;

import com.example.springapp.exceptions.CityNotFound;
import com.example.springapp.exceptions.IsNoRecordWithTempForToday;
import com.example.springapp.exceptions.JsonObjectNotFound;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.springapp.service.FactoryWeather.mapIdReg;

public class WeatherService {
    public static List<Weather> listWeathers;

    public static Map<Date,Integer> getTemperatureForDayInCity(String city) throws CityNotFound {
        if(mapIdReg.containsKey(city)) {
            Map<Date,Integer> map = listWeathers.stream()
                    .filter(weather -> weather.getReg().equals(city) &&
                            weather.getDateTime().after(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant())))
                    .collect(Collectors.toMap(Weather::getDateTime, Weather::getTemp));
            if(map.isEmpty())
                throw new IsNoRecordWithTempForToday("Запись(-и) с температурой на сегодняшний день по городу "+city+" не найдены");
            else
                return map;
        }
        else
            throw new CityNotFound("Город не найден");
    }

    public static Weather addNewRecordWeather(String city,String body) throws ParseException {
        JSONObject jsonObj = new JSONObject(body);
        FactoryWeather fw = new FactoryWeather();
        boolean hasTemp = jsonObj.has("temp");
        boolean hasDateTime = jsonObj.has("dateTime");

        if (!(hasTemp || hasDateTime))
            throw new JsonObjectNotFound("В теле запроса отсутствуют температура c датой и временем");
        else if(!hasTemp)
            throw new JsonObjectNotFound("В теле запроса отсутствует температура");
        else if(!hasDateTime)
            throw new JsonObjectNotFound("В теле запроса отсутствует дата и время");
        else {
            int newTemp = jsonObj.getInt("temp");
            String newDateTime = jsonObj.getString("dateTime");
            Weather newRecordWeather = fw.createWeather(
                    city,
                    newTemp,
                    newDateTime
            );
            listWeathers.add(newRecordWeather);
            return newRecordWeather;
        }
    }

    public static Weather updateTemperatureInCity(String city,String body) throws ParseException, JsonObjectNotFound {
        JSONObject jsonObj = new JSONObject(body);
        FactoryWeather fw = new FactoryWeather();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        boolean hasTemp = jsonObj.has("temp");
        boolean hasDateTime = jsonObj.has("dateTime");

        if (!(hasTemp || hasDateTime))
            throw new JsonObjectNotFound("В теле запроса отсутствуют температура c датой и временем");
        else if(!hasTemp)
            throw new JsonObjectNotFound("В теле запроса отсутствует температура");
        else if(!hasDateTime)
            throw new JsonObjectNotFound("В теле запроса отсутствует дата и время");
        else {
            int newTemp =  jsonObj.getInt("temp");

            String newDateTimeString = jsonObj.getString("dateTime");
            int index = Integer.MAX_VALUE;

            for (int i = 0; i < listWeathers.size(); i++) {
                if (listWeathers.get(i).getReg().equals(city) && listWeathers.get(i).getDateTime().compareTo(ft.parse(newDateTimeString)) == 0)
                    index = i;
            }
            Weather weather;
            if (index != Integer.MAX_VALUE) {
                listWeathers.get(index).setTemp(newTemp);
                return listWeathers.get(index);
            } else {
                weather = fw.createWeather(city, newTemp, newDateTimeString);
                listWeathers.add(weather);
                return weather;
            }
        }
    }

    public static void deleteCityInformation (String city) throws CityNotFound {
        if(mapIdReg.containsKey(city)) {
            listWeathers.removeIf(weather -> weather.getReg().equals(city));
            mapIdReg.remove(city);
        }
        else
            throw new CityNotFound("Город не найден");
    }
}
