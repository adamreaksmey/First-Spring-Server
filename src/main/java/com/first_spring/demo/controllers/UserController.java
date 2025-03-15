package com.first_spring.demo.controllers;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.first_spring.demo.entities.users.User;
import com.first_spring.demo.response.GlobalApiResponse;
import com.first_spring.demo.services.users.UserService;

import jakarta.validation.Valid;

/**
 * This class is responsible for handling the user endpoints.
 * It is annotated with @RestController to indicate that it is a REST controller.
 * It is annotated with @RequestMapping to map the /api/users endpoint to the UserController.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = Logger.getLogger(UserController.class.getName());

    /**
     * This annotation is used to automatically inject the UserService bean.
     * It eliminates the need for manual setup of the UserService bean in the UserController.
     */
    @Autowired
    private UserService userService;

    /**
     * Create a new User
     * @param user The user to be created
     * @return The created user
     */
    @PostMapping
    public ResponseEntity<GlobalApiResponse> createUser(@Valid @RequestBody User user) {
        User savedUser = userService.saveUser(user);
        System.out.println("message ==>: " + savedUser);
        System.out.flush();
        return ResponseEntity.status(201).body(GlobalApiResponse.success("User created successfully", savedUser));
    }

    /**
     * Get a User by ID
     * @param id The ID of the user to be retrieved
     * @return The user with the given ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Get all Users
     * @return All users in the database
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    /**
     * Update a User
     * @param id The ID of the user to be updated
     * @param userDetails The updated user details
     * @return The updated user
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        Optional<User> userOptional = userService.getUserById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setName(userDetails.getName());
            user.setEmail(userDetails.getEmail());
            user.setCustomedId(userDetails.getCustomedId());
            User updatedUser = userService.saveUser(user);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
