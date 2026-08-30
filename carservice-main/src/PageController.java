package com.example.usermanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping({"/", "/index.html", "/login"})
    public String index() {
        return "index";
    }

    @GetMapping({"/register", "/register.html"})
    public String register() {
        return "register";
    }

    @GetMapping({"/admin-dashboard", "/admin-dashboard.html", "/admin"})
    public String adminDashboard() {
        return "admin-dashboard";
    }

    @GetMapping({"/user-dashboard", "/user-dashboard.html", "/portal"})
    public String userDashboard() {
        return "user-dashboard";
    }

    @GetMapping({"/edit-profile", "/edit-profile.html"})
    public String editProfile() {
        return "edit-profile";
    }
}
