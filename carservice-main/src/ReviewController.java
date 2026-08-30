package com.example.usermanagement.controller;

import com.example.usermanagement.model.Review;
import com.example.usermanagement.service.ReviewService;
import com.example.usermanagement.service.ServiceRecordService;
import com.example.usermanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired private ReviewService reviewService;
    @Autowired private UserService userService;
    @Autowired private ServiceRecordService recordService;

    @GetMapping("/api")
    @ResponseBody
    public java.util.List<com.example.usermanagement.model.Review> apiList() {
        return reviewService.getAllReviews();
    }

    @GetMapping
    public String list(@CookieValue(name = "userId", required = false) Long userId,
                       @CookieValue(name = "userRole", required = false) String role,
                       Model model) {
        if ("SUPER_ADMIN".equals(role) || "ADMIN".equals(role)) {
            return "redirect:/admin-dashboard";
        }
        model.addAttribute("userRole", role);
        if (userId != null) {
            model.addAttribute("reviews", reviewService.getAllReviews().stream()
                    .filter(r -> r.getUser() != null && r.getUser().getId().equals(userId))
                    .toList());
        }
        Double avg = reviewService.getAverageRating();
        model.addAttribute("avgRating", avg != null ? String.format("%.1f", avg) : "N/A");
        return "reviews/list";
    }

    @GetMapping("/new")
    public String showForm(@CookieValue(name = "userRole", required = false) String role, Model model) {
        model.addAttribute("review", new Review());
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("serviceRecords", recordService.getAllRecords());
        model.addAttribute("userRole", role);
        return "reviews/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Review review,
                       @RequestParam(required = false) Long userId,
                       @RequestParam(required = false) Long serviceRecordId,
                       @CookieValue(name = "userId", required = false) Long loggedInUserId,
                       @CookieValue(name = "userRole", required = false) String role) {
        if ("SUPER_ADMIN".equals(role) || "ADMIN".equals(role)) {
            if (userId != null) userService.getUserById(userId).ifPresent(review::setUser);
        } else if (loggedInUserId != null) {
            userService.getUserById(loggedInUserId).ifPresent(review::setUser);
        }
        if (serviceRecordId != null) recordService.getRecordById(serviceRecordId).ifPresent(review::setServiceRecord);
        reviewService.saveReview(review);
        if ("SUPER_ADMIN".equals(role) || "ADMIN".equals(role)) {
            return "redirect:/admin-dashboard";
        }
        return "redirect:/dashboard";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, @CookieValue(name = "userRole", required = false) String role, Model model) {
        model.addAttribute("review", reviewService.getReviewById(id).orElseThrow());
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("serviceRecords", recordService.getAllRecords());
        model.addAttribute("userRole", role);
        return "reviews/form";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable Long id, @ModelAttribute Review review,
                         @RequestParam(required = false) Long userId,
                         @RequestParam(required = false) Long serviceRecordId,
                         @CookieValue(name = "userId", required = false) Long loggedInUserId,
                         @CookieValue(name = "userRole", required = false) String role) {
        
        Review existing = reviewService.getReviewById(id).orElseThrow();
        if (!"SUPER_ADMIN".equals(role) && !"ADMIN".equals(role)) {
            if (existing.getUser() == null || !existing.getUser().getId().equals(loggedInUserId)) {
                return "redirect:/reviews";
            }
            review.setUser(existing.getUser());
        } else {
            if (userId != null) userService.getUserById(userId).ifPresent(review::setUser);
        }

        if (serviceRecordId != null) recordService.getRecordById(serviceRecordId).ifPresent(review::setServiceRecord);
        reviewService.updateReview(id, review);
        if ("SUPER_ADMIN".equals(role) || "ADMIN".equals(role)) {
            return "redirect:/admin-dashboard";
        }
        return "redirect:/dashboard";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id, 
                         @CookieValue(name = "userId", required = false) Long userId,
                         @CookieValue(name = "userRole", required = false) String role) {
        
        Review review = reviewService.getReviewById(id).orElse(null);
        if (review != null) {
            if (!"SUPER_ADMIN".equals(role) && !"ADMIN".equals(role)) {
                if (review.getUser() == null || !review.getUser().getId().equals(userId)) {
                    return "redirect:/reviews";
                }
            }
            reviewService.deleteReview(id);
        }

        if ("SUPER_ADMIN".equals(role) || "ADMIN".equals(role)) {
            return "redirect:/admin-dashboard";
        }
        return "redirect:/dashboard";
    }
}
