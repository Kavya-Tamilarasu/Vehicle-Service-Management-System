package com.example.usermanagement.controller;

import com.example.usermanagement.service.UserService;
import com.example.usermanagement.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.ui.Model;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private UserService userService;

    @ModelAttribute
    public void addUserInfoToModel(@CookieValue(name = "userId", required = false) Long userId,
                                  @CookieValue(name = "userRole", required = false) String role,
                                  Model model) {
        if (userId != null) {
            userService.getUserById(userId).ifPresent(user -> {
                model.addAttribute("currentUserId", user.getId());
                model.addAttribute("userName", user.getName());
                model.addAttribute("userEmail", user.getEmail());
                model.addAttribute("userRole", role);
                String initial = (user.getName() != null && !user.getName().isEmpty()) 
                                 ? user.getName().substring(0, 1).toUpperCase() : "U";
                model.addAttribute("userInitial", initial);
            });
        }
    }
}
