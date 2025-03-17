package com.first_spring.demo.initializers;

import org.springframework.boot.CommandLineRunner;

// import com.first_spring.demo.repositories.UserRepository;

/**
 * This class is responsible for initializing the application with some default users.
 * It implements CommandLineRunner to execute the initialization process when the application starts.
 * The UserRepository is used to save the default users to the database.
 */
public class UserInitializer implements CommandLineRunner {
    // @Autowired
    // private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        // userRepository.save(new User("John Doe", "john@example.com"));
        // userRepository.save(new User("Jane Doe", "jane@example.com"));
    }
}
