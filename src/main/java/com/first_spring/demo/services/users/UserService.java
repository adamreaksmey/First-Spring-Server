package com.first_spring.demo.services.users;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.first_spring.demo.entities.users.User;
import com.first_spring.demo.repositories.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    // Create or Update a User
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // Get a User by ID
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // Get all Users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Delete a User by ID
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }
}
