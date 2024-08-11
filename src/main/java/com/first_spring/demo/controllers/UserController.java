package com.first_spring.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.first_spring.demo.entities.users.User;
import com.first_spring.demo.repositories.UserRepository;

@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    public String getFirstUserName() {
        List<User> users = userRepository.findAll();

        // Check if the list is not empty
        if (!users.isEmpty()) {
            // Get the first user in the list
            User oneUser = users.get(0);
            // Return the first user's name
            return oneUser.getName();
        }

        System.out.println("No users found.");
        return "No user found";

    }

}
