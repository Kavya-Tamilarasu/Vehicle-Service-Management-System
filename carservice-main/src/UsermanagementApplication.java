package com.example.usermanagement;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UsermanagementApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
		dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
		
		SpringApplication.run(UsermanagementApplication.class, args);
	}
	
	@org.springframework.context.annotation.Bean
	org.springframework.boot.CommandLineRunner initAdmin(
			com.example.usermanagement.repository.UserRepository userRepository,
			org.springframework.security.crypto.password.PasswordEncoder passwordEncoder,
			org.springframework.jdbc.core.JdbcTemplate jdbcTemplate) {
		return args -> {
			try {
				jdbcTemplate.execute("ALTER TABLE users MODIFY COLUMN role VARCHAR(255)");
			} catch (Exception e) {
				System.out.println("Could not alter table: " + e.getMessage());
			}
			java.util.Optional<com.example.usermanagement.model.User> existing = userRepository.findByEmail("admin@gmail.com");
			com.example.usermanagement.model.User admin;
			if (existing.isEmpty()) {
				admin = new com.example.usermanagement.model.User();
				admin.setEmail("admin@gmail.com");
			} else {
				admin = existing.get();
			}
			admin.setName("Super Admin");
			admin.setPassword(passwordEncoder.encode("admin123"));
			admin.setRole(com.example.usermanagement.model.Role.SUPER_ADMIN);
			userRepository.save(admin);
		};
	}

}
