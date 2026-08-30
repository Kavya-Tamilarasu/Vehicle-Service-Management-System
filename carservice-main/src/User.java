package com.example.usermanagement.model;

import jakarta.persistence.*;
import com.example.usermanagement.model.Role;

@Entity
@Table(name = "users")
public class User extends Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER; 

    // Polymorphism: implementing abstract method
    @Override
    public String getRole() { return role.name(); }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPassword() { return password; }
    public void setPassword(String p) { this.password = p; }
    public void setRole(Role role) { this.role = role; }
}
