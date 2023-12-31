package com.georgeradu.bookstore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)

public class InvalidRequestException extends RuntimeException {
    private static final String invalidRequestTemplate = "Invalid request: %s";

    public InvalidRequestException(String message) {
        super(String.format(invalidRequestTemplate, message));
    }
}
