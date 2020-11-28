package com.mytrip.attractionservice.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.text.MessageFormat;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AttractionNotFoundException extends RuntimeException {

    public AttractionNotFoundException(final Long attractionId) {
        super(buildErrorMessage(attractionId));
    }

    private static String buildErrorMessage(final Long attractionId) {
        return MessageFormat.format("Unable to find attraction: {0}", attractionId);
    }
}
