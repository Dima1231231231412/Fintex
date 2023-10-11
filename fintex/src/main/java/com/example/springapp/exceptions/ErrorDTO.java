package com.example.springapp.exceptions;

public class ErrorDTO extends BaseServiceException {
    public ErrorDTO(int status, String message, int errorCode) {
        super(status, message, errorCode);
    }
}


