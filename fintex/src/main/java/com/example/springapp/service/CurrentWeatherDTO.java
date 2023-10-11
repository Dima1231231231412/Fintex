package com.example.springapp.service;

import lombok.Data;

@Data
public class CurrentWeatherDTO {
    private Location location;
    private Current current;

    @Data
    public static class Current {
        private int last_updated_epoch;
        private String last_updated;
        private float temp_c;
        private float temp_f;
        private int is_day;
        Condition condition;
        private float wind_mph;
        private float wind_kph;
        private int wind_degree;
        private String wind_dir;
        private float pressure_mb;
        private float pressure_in;
        private float precip_mm;
        private float precip_in;
        private int humidity;
        private int cloud;
        private float feelslike_c;
        private float feelslike_f;
        private float vis_km;
        private float vis_miles;
        private float uv;
        private float gust_mph;
        private float gust_kph;
    }
    @Data
    public static class Condition {
        private String text;
        private String icon;
        private int code;

    }
    @Data
    public static class Location {
        private String name;
        private String region;
        private String country;
        private float lat;
        private float lon;
        private String tz_id;
        private int localtime_epoch;
        private String localtime;
    }
}
