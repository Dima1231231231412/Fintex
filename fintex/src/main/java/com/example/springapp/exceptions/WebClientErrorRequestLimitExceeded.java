package com.example.springapp.exceptions;

import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
public class WebClientExceptions extends BaseServiceException{

    public WebClientExceptions(int status, String message, int errorCode) {
        super(status,message, errorCode);
    }



    public static WebClientExceptions webClientErrorRequestLimitExceeded() {
        return new WebClientExceptions(
                HttpStatus.TOO_MANY_REQUESTS.value(),
                "Превышен лимит запросов в час, повторите попытку позже",
                1200);
    }
}
