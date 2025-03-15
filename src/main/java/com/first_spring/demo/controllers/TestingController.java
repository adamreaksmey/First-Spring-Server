package com.first_spring.demo.controllers;

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
    public String hello() {
        return "Hello, Adam!";
    }
}
