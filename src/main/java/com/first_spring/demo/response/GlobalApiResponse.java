package com.first_spring.demo.response;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

/**
 * This class is used to wrap a response to the client.
 * It is annotated with @Getter and @Setter to generate the getters and setters.
 * It is annotated with @JsonInclude(JsonInclude.Include.NON_NULL) to exclude null fields from the response.
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GlobalApiResponse {

    private Instant timestamp;
    private int status;
    private boolean success;
    private String message;
    private Object data;
    private Object errors;

    public GlobalApiResponse(int status, boolean success, String message, Object data, Object errors) {
        this.timestamp = Instant.now();
        this.status = status;
        this.success = success;
        this.message = message;
        this.data = data;
        this.errors = errors;
    }

    // ✅ Success Response
    public static GlobalApiResponse success(String message, Object data) {
        return new GlobalApiResponse(200, true, message, data, null);
    }

    // ✅ Error Response
    public static GlobalApiResponse error(int status, String message, Object errors) {
        return new GlobalApiResponse(status, false, message, null, errors);
    }
}
