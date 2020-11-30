package com.mytrip.attractionservice.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.text.MessageFormat;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AttractionsInCityNotFound extends RuntimeException {

    public AttractionsInCityNotFound(final Long cityId) {
        super(buildErrorMessage(cityId));
    }

    private static String buildErrorMessage(final Long cityId) {
        return MessageFormat.format("Unable to find attraction in city: {0}", cityId);
    }
}