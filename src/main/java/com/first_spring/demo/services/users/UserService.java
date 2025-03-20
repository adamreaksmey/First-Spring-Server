package com.first_spring.demo.services.users;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.first_spring.demo.entities.users.User;
import com.first_spring.demo.exceptions.DuplicateResourceException;
import com.first_spring.demo.repositories.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // Inject password encoder

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Create or Update a User
    public User saveUser(User user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepository.save(user);
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateResourceException(ex);
        }
    }

    // Get a User by ID
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // Get logged in user
    public Optional<User> getAuthorizedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof UserDetails userDetails)) {
            return Optional.empty();
        }

        // Fetch full user details from database
        return findByUsername(userDetails.getUsername());
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
