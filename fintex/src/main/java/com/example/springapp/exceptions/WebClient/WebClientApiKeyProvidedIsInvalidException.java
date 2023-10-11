package com.example.springapp.exceptions.webClient;

import org.springframework.http.HttpStatus;

public class WebClientApiKeyProvidedIsInvalidException extends BaseWebClientException {
    public WebClientApiKeyProvidedIsInvalidException() {
        super(HttpStatus.UNAUTHORIZED.value(),
                "API key provided is invalid",
                2006);
    }
}
