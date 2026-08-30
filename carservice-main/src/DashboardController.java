package com.example.usermanagement.controller;

import com.example.usermanagement.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.CookieValue;
import com.example.usermanagement.model.Review;

@Controller
public class DashboardController {

    @Autowired private CarService carService;
    @Autowired private ServiceRecordService recordService;
    @Autowired private ServiceReminderService reminderService;
    @Autowired private ReviewService reviewService;
    @Autowired private SparePartService sparePartService;
    @Autowired private UserService userService;

    @GetMapping("/api/admin/stats")
    @org.springframework.web.bind.annotation.ResponseBody
    public java.util.Map<String, Object> getAdminStats() {
        java.util.Map<String, Object> stats = new java.util.HashMap<>();
        stats.put("totalCars", carService.getAllCars().size());
        stats.put("totalRecords", recordService.getAllRecords().size());
        stats.put("activeReminders", reminderService.getActiveReminders().size());
        stats.put("overdueReminders", reminderService.getOverdueReminders().size());
        
        java.util.List<Review> reviews = reviewService.getAllReviews();
        stats.put("totalReviews", reviews.size());
        Double avg = reviewService.getAverageRating();
        stats.put("avgRating", avg != null ? String.format("%.1f", avg) : "N/A");
        
        stats.put("lowStockParts", sparePartService.getLowStockParts().size());
        stats.put("totalParts", sparePartService.getAllParts().size());
        
        return stats;
    }

    @GetMapping("/api/user/stats")
    @org.springframework.web.bind.annotation.ResponseBody
    public java.util.Map<String, Object> getUserStats(@CookieValue(name = "userId", required = false) Long userId) {
        java.util.Map<String, Object> stats = new java.util.HashMap<>();
        if (userId == null) return stats;

        long userCars = carService.getAllCars().stream()
            .filter(c -> c.getOwner() != null && c.getOwner().getId().equals(userId)).count();
        long userRecords = recordService.getAllRecords().stream()
            .filter(r -> r.getCar() != null && r.getCar().getOwner() != null && r.getCar().getOwner().getId().equals(userId)).count();
        long userActiveReminders = reminderService.getActiveReminders().stream()
            .filter(r -> r.getCar() != null && r.getCar().getOwner() != null && r.getCar().getOwner().getId().equals(userId)).count();
        long userOverdueReminders = reminderService.getOverdueReminders().stream()
            .filter(r -> r.getCar() != null && r.getCar().getOwner() != null && r.getCar().getOwner().getId().equals(userId)).count();

        stats.put("totalCars", userCars);
        stats.put("totalRecords", userRecords);
        stats.put("activeReminders", userActiveReminders);
        stats.put("overdueReminders", userOverdueReminders);
        
        return stats;
    }

    @GetMapping("/dashboard")
    public String dashboard(@CookieValue(name = "userId", required = false) Long userId,
                            @CookieValue(name = "userRole", required = false) String role,
                            Model model) {
        if ("SUPER_ADMIN".equals(role) || "ADMIN".equals(role)) {
            return "redirect:/admin-dashboard";
        }
        
        model.addAttribute("userRole", role);
        model.addAttribute("userName", "User");
        model.addAttribute("userEmail", "");
        model.addAttribute("totalCars", 0);
        model.addAttribute("totalRecords", 0);
        model.addAttribute("activeReminders", 0);
        model.addAttribute("overdueReminders", 0);
        model.addAttribute("totalReviews", 0);
        model.addAttribute("avgRating", "N/A");

        if (userId != null) {
            com.example.usermanagement.model.User u = userService.getUserById(userId).orElse(null);
            if (u != null) {
                model.addAttribute("userName", u.getName());
                model.addAttribute("userEmail", u.getEmail());
            }

            long userCars = carService.getAllCars().stream()
                .filter(c -> c.getOwner() != null && c.getOwner().getId().equals(userId)).count();
            long userRecords = recordService.getAllRecords().stream()
                .filter(r -> r.getCar() != null && r.getCar().getOwner() != null && r.getCar().getOwner().getId().equals(userId)).count();
            long userActiveReminders = reminderService.getActiveReminders().stream()
                .filter(r -> r.getCar() != null && r.getCar().getOwner() != null && r.getCar().getOwner().getId().equals(userId)).count();
            long userOverdueReminders = reminderService.getOverdueReminders().stream()
                .filter(r -> r.getCar() != null && r.getCar().getOwner() != null && r.getCar().getOwner().getId().equals(userId)).count();
            java.util.List<com.example.usermanagement.model.Review> userReviews = reviewService.getAllReviews().stream()
                .filter(r -> r.getUser() != null && r.getUser().getId().equals(userId)).toList();
            double avg = userReviews.isEmpty() ? 0.0 : userReviews.stream().mapToInt(com.example.usermanagement.model.Review::getRating).average().orElse(0.0);
            
            model.addAttribute("totalCars", userCars);
            model.addAttribute("totalRecords", userRecords);
            model.addAttribute("activeReminders", userActiveReminders);
            model.addAttribute("overdueReminders", userOverdueReminders);
            model.addAttribute("totalReviews", userReviews.size());
            model.addAttribute("avgRating", userReviews.isEmpty() ? "N/A" : String.format("%.1f", avg));
        }
        return "user-dashboard";
    }
}
