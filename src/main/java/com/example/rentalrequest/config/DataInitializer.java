package com.example.rentalrequest.config;

import com.example.rentalrequest.model.User;
import com.example.rentalrequest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.findByUsername("sri").isEmpty()) {
            User admin = new User();
            admin.setUsername("sri");
            admin.setPassword(passwordEncoder.encode("ram123"));
            admin.setRole(User.Role.ADMIN);
            userRepository.save(admin);
        }
    }
}


