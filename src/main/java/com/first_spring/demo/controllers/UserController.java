package com.first_spring.demo.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.first_spring.demo.entities.users.User;
import com.first_spring.demo.response.GlobalApiResponse;
import com.first_spring.demo.security.annotations.Protected;
import com.first_spring.demo.services.users.UserService;

import jakarta.validation.Valid;

/**
 * This class is responsible for handling the user endpoints.
 * It is annotated with @RestController to indicate that it is a REST
 * controller.
 * It is annotated with @RequestMapping to map the /api/users endpoint to the
 * UserController.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {
    /**
     * This annotation is used to automatically inject the UserService bean.
     * It eliminates the need for manual setup of the UserService bean in the
     * UserController.
     */
    @Autowired
    private UserService userService;

    /**
     * Create a new User
     * 
     * @param user The user to be created
     * @return The created user
     */
    @PostMapping
    public ResponseEntity<GlobalApiResponse> createUser(@Valid @RequestBody User user) {
        User savedUser = userService.saveUser(user);
        return ResponseEntity.status(201).body(GlobalApiResponse.success("User created successfully", savedUser));
    }

    /**
     * Get a User by ID
     * 
     * @param id The ID of the user to be retrieved
     * @return The user with the given ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<GlobalApiResponse> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        if (user.isEmpty()) {
            return ResponseEntity.status(404).body(GlobalApiResponse.error(404, "User not found", null));
        }

        return ResponseEntity.ok(GlobalApiResponse.success("User retrieved successfully", user));
    }

    /**
     * Get all Users
     * 
     * @return All users in the database
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    /**
     * Update a User
     * 
     * @param id          The ID of the user to be updated
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

    /**
     * Delete a User
     * 
     * @param id The ID of the user to be deleted
     * @return The response entity
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<GlobalApiResponse> deleteUser(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        if (user.isEmpty()) {
            return ResponseEntity.status(404).body(GlobalApiResponse.error(404, "User not found", null));
        }

        userService.deleteUserById(id);
        return ResponseEntity.ok(GlobalApiResponse.success("User deleted successfully", null));
    }

    /**
     * Get the currently authenticated user
     * 
     * @return The currently authenticated user
     */
    @GetMapping("/me")
    @Protected
    public String getAuthorizedUser() {
        return "hello world";
    }
}
