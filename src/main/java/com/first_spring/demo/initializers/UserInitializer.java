package com.first_spring.demo.initializers;

import org.springframework.boot.CommandLineRunner;

import com.first_spring.demo.entities.users.User;
import com.first_spring.demo.repositories.UserRepository;

// @Component
public class UserInitializer implements CommandLineRunner {
    // @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        userRepository.save(new User("John Doe", "john@example.com"));
        userRepository.save(new User("Jane Doe", "jane@example.com"));
    }
}
