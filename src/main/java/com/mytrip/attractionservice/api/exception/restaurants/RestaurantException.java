package com.mytrip.attractionservice.api.exception.restaurants;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class RestaurantException extends RuntimeException {

    public RestaurantException() {
        super(buildErrorMessage());
    }

    private static String buildErrorMessage() {
        return "Unexpected exception";
    }
}
