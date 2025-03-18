package com.first_spring.demo.exceptions;

import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(DataIntegrityViolationException ex) {
        super(parseMessage(ex));
    }

    private static String parseMessage(DataIntegrityViolationException ex) {
        String message = Optional.ofNullable(ex.getRootCause())
                .map(Throwable::getMessage)
                .orElse(ex.getMessage());

        if (message.contains("uk")) {
            if (message.contains("username")) {
                return "Username has already been taken.";
            }
            if (message.contains("email")) {
                return "Email has already been taken.";
            }
        }
        return "Duplicate resource violation.";
    }
}
