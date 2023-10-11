package com.example.springapp.exceptions;

import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
public class WebClientErrorRequestLimitExceeded extends BaseWebClientException{
    public WebClientErrorRequestLimitExceeded() {
        super(HttpStatus.TOO_MANY_REQUESTS.value(),
                "Превышен лимит запросов в час, повторите попытку позже",
                1200);
    }
}
