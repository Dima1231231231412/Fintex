package com.example.springapp.exceptions.webClient;

import org.springframework.http.HttpStatus;

public class WebClientApiKeyNotProvidedException extends BaseWebClientException {
    public WebClientApiKeyNotProvidedException(){
        super(HttpStatus.UNAUTHORIZED.value(),
                "API key not provided",
                1002);
    }
}
