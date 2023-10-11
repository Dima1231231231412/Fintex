package com.example.springapp.exceptions;

import org.springframework.http.HttpStatus;


public class ErrorResponse extends BaseServiceException {
    public ErrorResponse(int status, String message, int errorCode) {
        super(status,message, errorCode);
    }

    //Запись с температурой на сегодняшний день по городу не найдена
    public static ErrorResponse IsNoRecordWithTempForToday(String city) {
        return new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Запись(-и) с температурой на сегодняшний день по городу " + city + " не найдены",
                405);
    }

    // город не найден
    public static ErrorResponse notFoundCity() {
        return new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Город не найден",
                406);

    }
}

