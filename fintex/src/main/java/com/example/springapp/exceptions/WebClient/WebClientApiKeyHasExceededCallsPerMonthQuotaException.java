package com.example.springapp.exceptions.webClient;

import org.springframework.http.HttpStatus;

public class WebClientApiKeyHasExceededCallsPerMonthQuotaException extends BaseWebClientException {
    public WebClientApiKeyHasExceededCallsPerMonthQuotaException() {
        super(HttpStatus.FORBIDDEN.value(),
                "API key has exceeded calls per month quota",
                2007);
    }
}
