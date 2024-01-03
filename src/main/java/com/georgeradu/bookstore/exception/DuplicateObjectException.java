package com.georgeradu.bookstore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class DuplicateObjectException extends RuntimeException {
    private static final String duplicateObjectTemplate = "Object: %s already exists";

    public DuplicateObjectException(String object) {
        super(String.format(duplicateObjectTemplate, object));
    }
}