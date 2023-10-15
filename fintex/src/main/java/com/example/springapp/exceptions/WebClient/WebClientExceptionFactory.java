package com.example.springapp.exceptions.webClient;

public class WebClientExceptionFactory {
    //Получение ошибки в зависимости от её кода
    public static void getException(int errorCode) {
        switch (errorCode) {
            case 1002 -> throw new WebClientApiKeyNotProvidedException();
            case 2006 -> throw new WebClientApiKeyProvidedIsInvalidException();
            case 1003 -> throw new WebClientParameterQNotProvidedException();
            case 1005 -> throw new WebClientApiRequestUrlIsInvalidException();
            case 1006 -> throw new WebClientLocationNotFoundException();
            case 2007 -> throw new WebClientApiKeyHasExceededCallsPerMonthQuotaException();
            case 2008 -> throw new WebClientApiKeyHasBeenDisabledException();
            case 2009 -> throw new WebClientApiKeyDoesNotHaveAccessToTheResourceException();
            default -> throw new RuntimeException("Unknown error code");
        }
    }
}
