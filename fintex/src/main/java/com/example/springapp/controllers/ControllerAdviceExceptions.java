package com.example.springapp.controllers;

import com.example.springapp.exceptions.BaseServiceException;
import com.example.springapp.exceptions.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ControllerAdviceExceptions {
    @ExceptionHandler(BaseServiceException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(ErrorResponse ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getStatus(), ex.getErrorMessage(), ex.getErrorCode());
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(ex.getStatus()));
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorResponse> unhandleException(ErrorResponse ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getStatus(), ex.getErrorMessage(), ex.getErrorCode());
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(ex.getStatus()));
    }
}

