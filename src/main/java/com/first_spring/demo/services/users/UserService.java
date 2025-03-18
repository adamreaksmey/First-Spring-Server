package com.first_spring.demo.services.users;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.first_spring.demo.entities.users.User;
import com.first_spring.demo.exceptions.DuplicateResourceException;
import com.first_spring.demo.repositories.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    // Create or Update a User
    public User saveUser(User user) {
        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateResourceException(ex);
        }
    }

    // Get a User by ID
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // Get user by email
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    // Get all Users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Delete a User by ID
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    // customed delete by userId
    public void _deleteUserById(Long id) {
        userRepository.deleteByCustomedId(id);
    }

    // Find a User by username
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
