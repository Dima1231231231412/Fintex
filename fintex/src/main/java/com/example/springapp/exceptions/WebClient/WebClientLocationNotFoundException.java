package com.example.springapp.exceptions.webClient;

import org.springframework.http.HttpStatus;

public class WebClientLocationNotFoundException extends BaseWebClientException {
    public WebClientLocationNotFoundException() {
        super(HttpStatus.BAD_REQUEST.value(),
                "No location found matching parameter 'q'",
                1006);
    }
}
