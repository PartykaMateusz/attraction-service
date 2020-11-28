package com.mytrip.attractionservice.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.text.MessageFormat;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class AttractionException extends RuntimeException {

    public AttractionException() {
        super(buildErrorMessage());
    }

    private static String buildErrorMessage() {
        return "Unexpected exception";
    }
}
