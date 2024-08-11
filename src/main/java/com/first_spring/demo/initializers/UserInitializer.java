package com.first_spring.demo.initializers;

import com.first_spring.demo.entities.users.User;
import com.first_spring.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class UserInitializer implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        userRepository.save(new User("John Doe", "john@example.com"));
        userRepository.save(new User("Jane Doe", "jane@example.com"));
    }
}
