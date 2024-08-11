package com.first_spring.demo.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestingController {
    @GetMapping("/hello")
    public String hello() {
        return "Hello, Adam!";
    }
}
