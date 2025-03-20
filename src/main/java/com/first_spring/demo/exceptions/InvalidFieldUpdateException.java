package com.first_spring.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST) // Returns 400 Bad Request automatically
public class InvalidFieldUpdateException extends RuntimeException {

    public InvalidFieldUpdateException(String fieldName, String message) {
        super("Invalid update for field '" + fieldName + "': " + message);
    }
}
