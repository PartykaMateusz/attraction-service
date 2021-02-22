package com.mytrip.attractionservice.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.text.MessageFormat;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AttractionNotFound extends RuntimeException {

    public AttractionNotFound(final String attractionName) {
        super(buildErrorMessage(attractionName));
    }

    private static String buildErrorMessage(final String attractionName) {
        return MessageFormat.format("Unable to find attraction: {0}", attractionName);
    }
}
