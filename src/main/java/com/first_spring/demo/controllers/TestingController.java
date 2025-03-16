package com.first_spring.demo.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class is responsible for handling the testing endpoint.
 * It is annotated with @RestController to indicate that it is a REST controller.
 * It is annotated with @GetMapping to map the GET request to the hello method.
 */
@RestController
public class TestingController {
    @GetMapping("/hello")
    public Map<String, String> hello() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Hello, Adam!");
        return response;
    }
}
