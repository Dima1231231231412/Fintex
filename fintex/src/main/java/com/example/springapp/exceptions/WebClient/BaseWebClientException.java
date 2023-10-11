package com.example.springapp.exceptions.webClient;

import com.example.springapp.exceptions.BaseServiceException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class BaseWebClientException extends RuntimeException{
    private int status;
    private String message;
    private int errorCode;

    //Исключение не нужной для пользователя информации при ошибки
    @JsonIgnore
    @Override
    public StackTraceElement[] getStackTrace() {
        return super.getStackTrace();
    }
    @JsonIgnore
    @Override
    public Throwable getCause() {
        return super.getCause();
    }
    @JsonIgnore
    @Override
    public String getLocalizedMessage() {
        return super.getLocalizedMessage();
    }
}
