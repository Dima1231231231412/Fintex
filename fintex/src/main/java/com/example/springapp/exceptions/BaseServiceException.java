package com.example.springapp.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class BaseServiceException extends RuntimeException{
    private final int status;
    private final String message;
    private final int errorCode;

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
