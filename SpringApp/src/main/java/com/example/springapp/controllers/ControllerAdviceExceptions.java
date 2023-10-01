package com.example.springapp.controllers;

import com.example.springapp.exceptions.CityNotFound;
import com.example.springapp.exceptions.IsNoRecordWithTempForToday;
import com.example.springapp.exceptions.JsonObjectNotFound;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ControllerAdviceExceptions { ;
    @ExceptionHandler
    public ResponseEntity<String> cityNotFound(CityNotFound ex){
        log.error(ex.getMessage(),ex);
        return new ResponseEntity<>(ex.printError(ex.getMessage(),404), HttpStatus.NOT_FOUND);
    }



    @ExceptionHandler
    public ResponseEntity<String> jsonObjectNotFound(JsonObjectNotFound ex){
        log.error(ex.getMessage(),ex);
        return new ResponseEntity<>(ex.printError(ex.getMessage(),400), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> IsNoRecordWithTempForToday(IsNoRecordWithTempForToday ex){
        log.error(ex.getMessage(),ex);
        return new ResponseEntity<>(ex.printError(ex.getMessage(), 404), HttpStatus.NOT_FOUND);
    }
}
