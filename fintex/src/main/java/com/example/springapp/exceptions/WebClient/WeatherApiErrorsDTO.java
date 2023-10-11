package com.example.springapp.exceptions.webClient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherApiErrorsDTO {
    private Error error;

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Error {
        private int code;
        private String message;

    }
}

