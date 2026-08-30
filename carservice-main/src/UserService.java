package com.example.usermanagement.service;

import com.example.usermanagement.model.Role;
import com.example.usermanagement.model.User;
import com.example.usermanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // CREATE — register new user (always enforces USER role; admins can only be promoted from admin panel)
    public User registerUser(User user) {
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // READ — get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // READ — get one user by id
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // UPDATE — update user details
    public User updateUser(Long id, User updatedUser) {
        User user = userRepository.findById(id).orElseThrow();
        user.setName(updatedUser.getName());
        user.setEmail(updatedUser.getEmail());
        user.setPhone(updatedUser.getPhone());
        
        // Only update password if a new one is provided and it's not empty
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }
        
        return userRepository.save(user);
    }

    // UPDATE role
    public User updateRole(Long id, String role) {
        User user = userRepository.findById(id).orElseThrow();
        user.setRole(Role.valueOf(role));
        return userRepository.save(user);
    }

    // DELETE — remove a user
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // LOGIN — check email and password
    public Optional<User> login(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            return user;
        }
        return Optional.empty();
    }
}