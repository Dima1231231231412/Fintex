package com.example.springapp.exceptions.webClient;

import org.springframework.http.HttpStatus;

public class WebClientParameterQNotProvidedException extends BaseWebClientException {
    public WebClientParameterQNotProvidedException(){
        super(HttpStatus.BAD_REQUEST.value(),
                "Parameter 'q' not provided",
                1003);
    }
}
