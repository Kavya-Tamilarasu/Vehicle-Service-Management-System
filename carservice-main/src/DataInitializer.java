package com.example.usermanagement;

import com.example.usermanagement.model.Role;
import com.example.usermanagement.model.User;
import com.example.usermanagement.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner seedDefaultAdmin(UserRepository userRepository,
                                              PasswordEncoder passwordEncoder) {
        return args -> {
            final String adminEmail    = "admin";   // login with username: admin
            final String adminPassword = "admin";

            User admin = userRepository.findByEmail(adminEmail).orElse(new User());

            // Always enforce correct credentials and role (fixes stale/wrong data on every startup)
            admin.setName("Administrator");
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setPhone("");
            admin.setRole(Role.SUPER_ADMIN);
            userRepository.save(admin);

            System.out.println("✅ Default admin account ready: " + adminEmail + " / " + adminPassword);
        };
    }
}
