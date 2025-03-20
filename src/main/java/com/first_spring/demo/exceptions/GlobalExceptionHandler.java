package com.first_spring.demo.exceptions;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.first_spring.demo.response.GlobalApiResponse;

/**
 * This class is responsible for handling global exceptions.
 * It is annotated with @RestControllerAdvice to handle exceptions in all
 * controllers.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * This method is responsible for handling data integrity violation exceptions.
     * It is annotated with @ExceptionHandler to handle
     * DataIntegrityViolationException.
     * 
     * @param ex The DataIntegrityViolationException to be handled
     * @return The ResponseEntity with the error
     */
    public ResponseEntity<GlobalApiResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        Map<String, String> errorResponse = new HashMap<>();

        String message = Optional.ofNullable(ex.getRootCause())
                .map(Throwable::getMessage)
                .orElse(ex.getMessage()); // Get the root cause message

        if (message.contains("not-null property references a null")) {
            String fieldName = message.substring(message.lastIndexOf(".") + 1); // Extract the field name
            errorResponse.put(fieldName, "This field cannot be null.");
        } else if (message.contains("duplicate key value")) {
            errorResponse.put("error", "This value must be unique.");
        } else {
            errorResponse.put("error", "A database error occurred.");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(GlobalApiResponse.error(HttpStatus.BAD_REQUEST.value(), "Data integrity violation",
                        errorResponse));
    }

    /**
     * This method is responsible for handling validation exceptions.
     * It is annotated with @ExceptionHandler to handle
     * MethodArgumentNotValidException.
     * 
     * @param ex The MethodArgumentNotValidException to be handled
     * @return The ResponseEntity with the errors
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GlobalApiResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity.badRequest()
                .body(GlobalApiResponse.error(HttpStatus.BAD_REQUEST.value(), "Validation failed", errors));
    }

    /**
     * This method is responsible for handling general exceptions.
     * It is annotated with @ExceptionHandler to handle Exception.
     * 
     * @param ex The Exception to be handled
     * @return The ResponseEntity with the error
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<GlobalApiResponse> handleGeneralExceptions(Exception ex) {
        System.out.println("coming from this one" + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(GlobalApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Something went wrong",
                        ex.getMessage()));
    }

    /**
     * This method is responsible for handling empty request body exceptions.
     * It is annotated with @ExceptionHandler to handle
     * HttpMessageNotReadableException.
     * 
     * @param ex The HttpMessageNotReadableException to be handled
     * @return The ResponseEntity with the error
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<GlobalApiResponse> handleEmptyRequestBody(HttpMessageNotReadableException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Request body cannot be empty");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(GlobalApiResponse.error(HttpStatus.BAD_REQUEST.value(), "Request body cannot be empty",
                        errorResponse));
    }

    /*
     * This method is responsible for handling invalid field update exceptions.
     * 
     * @param ex The InvalidFieldUpdateException to be handled
     */
    @ExceptionHandler(InvalidFieldUpdateException.class)
    public ResponseEntity<GlobalApiResponse> handleInvalidFieldUpdate(InvalidFieldUpdateException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(GlobalApiResponse.error(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), null));
    }
}
