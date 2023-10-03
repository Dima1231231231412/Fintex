package com.example.springapp.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class BaseServiceException extends RuntimeException{
    private final int status;
    private final String errorMessage;
    private final int errorCode;
}
