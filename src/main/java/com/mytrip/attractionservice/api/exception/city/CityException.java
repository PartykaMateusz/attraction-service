package com.mytrip.attractionservice.api.exception.city;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class CityException extends RuntimeException {

    public CityException() {
        super(buildErrorMessage());
    }

    private static String buildErrorMessage() {
        return "Unexpected exception";
    }
}
