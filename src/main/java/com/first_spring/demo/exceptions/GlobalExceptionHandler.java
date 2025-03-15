package com.first_spring.demo.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * This class is responsible for handling global exceptions.
 * It is annotated with @RestControllerAdvice to handle exceptions in all
 * controllers.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * This method is responsible for handling validation exceptions.
     * It is annotated with @ExceptionHandler to handle
     * MethodArgumentNotValidException.
     * 
     * @param ex The MethodArgumentNotValidException to be handled
     * @return The ResponseEntity with the errors
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity.badRequest().body(errors);
    }

    /**
     * This method is responsible for handling general exceptions.
     * It is annotated with @ExceptionHandler to handle Exception.
     * 
     * @param ex The Exception to be handled
     * @return The ResponseEntity with the error
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneralExceptions(Exception ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * This method is responsible for handling empty request body exceptions.
     * It is annotated with @ExceptionHandler to handle HttpMessageNotReadableException.
     * 
     * @param ex The HttpMessageNotReadableException to be handled
     * @return The ResponseEntity with the error
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleEmptyRequestBody(HttpMessageNotReadableException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Request body cannot be empty");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
