package com.example.usermanagement.controller;

import com.example.usermanagement.model.Role;
import com.example.usermanagement.model.User;
import com.example.usermanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    /** Null out the password hash before sending to the browser */
    private User sanitize(User u) {
        u.setPassword(null);
        return u;
    }

    // CREATE — POST /api/users/register
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        return ResponseEntity.ok(sanitize(userService.registerUser(user)));
    }

    // READ — GET /api/users
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers().stream().map(this::sanitize).toList();
    }

    // READ — GET /api/users/1
    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(u -> ResponseEntity.ok(sanitize(u)))
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE — PUT /api/users/1
    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable Long id, @RequestBody User user) {
        return ResponseEntity.ok(sanitize(userService.updateUser(id, user)));
    }

    // DELETE — DELETE /api/users/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    // LOGIN endpoint — POST /api/users/login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return userService.login(request.email, request.password)
                .map(user -> ResponseEntity.ok((Object) sanitize(user)))
                .orElse(ResponseEntity.status(401).build());
    }

    // UPDATE role — PUT /api/users/1/role?role=ADMIN
    @PutMapping("/{id}/role")
    public ResponseEntity<User> updateRole(@PathVariable Long id, @RequestParam String role) {
        return ResponseEntity.ok(sanitize(userService.updateRole(id, role)));
    }
}

class LoginRequest {
    public String email;
    public String password;
}