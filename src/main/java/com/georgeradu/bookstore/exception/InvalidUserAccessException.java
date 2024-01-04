package com.georgeradu.bookstore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)

public class InvalidUserAccessException extends RuntimeException {
    private static final String invalidUserAccessTemplate = "User does not have access to this resource or action: %s";

    public InvalidUserAccessException(String user) {
        super(String.format(invalidUserAccessTemplate, user));
    }
}
