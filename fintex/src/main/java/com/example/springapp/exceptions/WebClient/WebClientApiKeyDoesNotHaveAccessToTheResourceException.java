package com.example.springapp.exceptions.webClient;

import org.springframework.http.HttpStatus;

public class WebClientApiKeyDoesNotHaveAccessToTheResourceException extends BaseWebClientException {
    public WebClientApiKeyDoesNotHaveAccessToTheResourceException() {
        super(HttpStatus.FORBIDDEN.value(),
                "API key has been disabled",
                2009);
    }
}
