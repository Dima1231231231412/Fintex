package com.example.springapp.exceptions;

import org.springframework.http.HttpStatus;

public class WebClientErrorCodeFactory {
    //Получение статуса в зависимости от кода ошибки
    public static int getStatusByErrorCode(int errorCode) {
        return switch (errorCode) {
            case 1002 -> 
            case 2006 -> HttpStatus.UNAUTHORIZED.value();
            case 1003, 1005, 1006, 9000, 9001 -> HttpStatus.BAD_REQUEST.value();
            case 2007, 2008, 2009 -> HttpStatus.FORBIDDEN.value();
            default -> HttpStatus.INTERNAL_SERVER_ERROR.value();
        };
    }
}
