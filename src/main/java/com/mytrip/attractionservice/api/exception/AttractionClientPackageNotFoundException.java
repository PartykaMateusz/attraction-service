package com.mytrip.attractionservice.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.text.MessageFormat;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AttractionClientPackageNotFoundException extends RuntimeException {

    public AttractionClientPackageNotFoundException(final Long cityId) {
        super(buildErrorMessage(cityId));
    }

    public AttractionClientPackageNotFoundException(final String attractionName) {
        super(buildErrorMessage(attractionName));
    }

    private static String buildErrorMessage(final String attractionName) {
        return MessageFormat.format("Unable to find package for attraction: {0}", attractionName);
    }

    private static String buildErrorMessage(final Long cityId) {
        return MessageFormat.format("Unable to find package for city: {0}", cityId);
    }
}
