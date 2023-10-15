package com.example.springapp.controllers;

import com.example.springapp.exceptions.BaseServiceException;
import com.example.springapp.exceptions.MessageDTOException;
import com.example.springapp.exceptions.webClient.BaseWebClientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerAdviceExceptions {
    @ExceptionHandler(BaseServiceException.class)
    public ResponseEntity<MessageDTOException> handleBaseException(BaseServiceException ex) {
        MessageDTOException messageDTOException = new MessageDTOException(ex.getMessage());
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(messageDTOException, HttpStatus.valueOf(ex.getStatus()));
    }

    @ExceptionHandler(BaseWebClientException.class)
    public ResponseEntity<MessageDTOException> handleWebClientException(BaseWebClientException ex) {
        MessageDTOException messageDTOException = new MessageDTOException(ex.getMessage());
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(messageDTOException, HttpStatus.valueOf(ex.getStatus()));
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Object> unhandleException(Throwable ex) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
    }


}

