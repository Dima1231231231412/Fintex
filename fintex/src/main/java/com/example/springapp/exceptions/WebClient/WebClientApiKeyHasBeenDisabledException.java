package com.example.springapp.exceptions.webClient;

import org.springframework.http.HttpStatus;

public class WebClientApiKeyHasBeenDisabledException extends BaseWebClientException {
    public WebClientApiKeyHasBeenDisabledException() {
        super(HttpStatus.FORBIDDEN.value(),
                "API key has been disabled",
                2008);
    }
}
