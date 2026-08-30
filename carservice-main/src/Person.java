package com.example.usermanagement.model;

import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class Person {

    private String name;
    private String email;
    private String phone;

    public abstract String getRole();

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}
