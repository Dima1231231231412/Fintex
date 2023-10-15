package com.example.springapp.exceptions.webClient;

import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
public class WebClientRequestLimitExceededException extends BaseWebClientException {
    public WebClientRequestLimitExceededException() {
        super(HttpStatus.TOO_MANY_REQUESTS.value(),
                "Превышен лимит запросов в час, повторите попытку позже",
                1200);
    }
}
