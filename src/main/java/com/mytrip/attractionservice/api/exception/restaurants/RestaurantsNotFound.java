package com.mytrip.attractionservice.api.exception.restaurants;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.text.MessageFormat;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RestaurantsNotFound extends RuntimeException {

    public RestaurantsNotFound(final String latitude, final String longitude) {
        super(buildErrorMessage(latitude, longitude));
    }

    private static String buildErrorMessage(final String latitude, final String longitude) {
        return MessageFormat.format("Unable to find restaurants by latitude: {0}, longitude: {}", latitude, longitude);
    }
}
