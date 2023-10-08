package com.example.springapp.controllers;

import com.example.springapp.exceptions.ErrorResponse;
import com.example.springapp.exceptions.WebClientExceptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerAdviceExceptions {
    @ExceptionHandler(ErrorResponse.class)
    public ResponseEntity<Object> handleBaseException(ErrorResponse ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getStatus(), ex.getMessage(), ex.getErrorCode());
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(ex.getStatus()));
    }

    @ExceptionHandler(WebClientExceptions.class)
    public ResponseEntity<Object> handleInternalErrorException(WebClientExceptions ex) {
        WebClientExceptions webClientExceptions = new WebClientExceptions(ex.getStatus(),ex.getMessage(),ex.getErrorCode());
        log.error(ex.getMessage(),ex);
        return new ResponseEntity<>(webClientExceptions,HttpStatus.valueOf(ex.getStatus()));
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Object> unhandleException(Throwable ex) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
    }


}

