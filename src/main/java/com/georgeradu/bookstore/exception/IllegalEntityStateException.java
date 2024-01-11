package com.georgeradu.bookstore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class IllegalEntityStateException extends RuntimeException {
    private static final String errorMessage = "Illegal entity state: %s";

    public IllegalEntityStateException(String field) {
        super(String.format(errorMessage, field));
    }
}
