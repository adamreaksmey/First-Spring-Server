package com.first_spring.demo.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.first_spring.demo.entities.users.User;
import com.first_spring.demo.response.GlobalApiResponse;
import com.first_spring.demo.security.JwtUtil;
import com.first_spring.demo.services.users.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final UserDetailsService userDetailsService;

    /**
     * Constructor
     * 
     * @param authenticationManager
     * @param jwtUtil
     * @param userService
     * @param userDetailsService
     */
    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserService userService,
            UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Register a new user
     * 
     * @param user
     * @return ResponseEntity<GlobalApiResponse>
     */
    @PostMapping("/register")
    public ResponseEntity<GlobalApiResponse> register(@RequestBody User user) {
        User savedUser = userService.saveUser(user);
        return ResponseEntity.ok(GlobalApiResponse.success("User registered successfully", savedUser));
    }

    /**
     * Login a user
     * 
     * @param loginRequest
     * @return ResponseEntity<GlobalApiResponse>
     */
    @PostMapping("/login")
    public ResponseEntity<GlobalApiResponse> login(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String token = jwtUtil.generateToken(userDetails.getUsername());

        return ResponseEntity.ok(GlobalApiResponse.success("Login successful", Map.of("token", token)));
    }
}
