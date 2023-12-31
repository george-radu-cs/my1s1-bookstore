package com.georgeradu.bookstore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidLoginException extends RuntimeException {
    private static final String errorMessage = "Invalid user credentials";

    public InvalidLoginException() {
        super(errorMessage);
    }
}
