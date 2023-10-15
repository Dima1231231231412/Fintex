package com.example.springapp.exceptions.webClient;

import org.springframework.http.HttpStatus;

public class WebClientApiRequestUrlIsInvalidException extends BaseWebClientException {
    public WebClientApiRequestUrlIsInvalidException() {
        super(HttpStatus.BAD_REQUEST.value(),
                "API request url is invalid",
                1005);
    }
}
