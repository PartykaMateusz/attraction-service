package com.mytrip.attractionservice.api.exception.city;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.text.MessageFormat;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CityNotFound extends RuntimeException {

    public CityNotFound(final String cityName) {
        super(buildErrorMessage(cityName));
    }

    private static String buildErrorMessage(final String cityName) {
        return MessageFormat.format("Unable to find city: {0}", cityName);
    }
}
