package com.mytrip.attractionservice.api.exception.restaurants;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.text.MessageFormat;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RestaurantClientPackageNotFoundException extends RuntimeException {

    public RestaurantClientPackageNotFoundException(final String latitude, final String longitude) {
        super(buildErrorMessage(latitude, longitude));
    }

    private static String buildErrorMessage(final String latitude, final String longitude) {
        return MessageFormat.format("Unable to find restaurant for latitude: {0}, longitude", latitude, longitude);
    }

}
