package com.example.usermanagement.controller;

import com.example.usermanagement.model.ReminderStatus;
import com.example.usermanagement.model.ServiceReminder;
import com.example.usermanagement.service.CarService;
import com.example.usermanagement.service.ServiceReminderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/reminders")
public class ServiceReminderController {

    @Autowired private ServiceReminderService reminderService;
    @Autowired private CarService carService;

    @GetMapping("/api")
    @ResponseBody
    public java.util.List<com.example.usermanagement.model.ServiceReminder> apiList(@RequestParam(required = false) Long carId) {
        if (carId != null) {
            return reminderService.getAllReminders().stream()
                    .filter(r -> r.getCar() != null && r.getCar().getId().equals(carId))
                    .toList();
        }
        return reminderService.getAllReminders();
    }

    @GetMapping("/alerts")
    @ResponseBody
    public java.util.List<String> getAlerts(@CookieValue(name = "userId", required = false) Long userId) {
        if (userId == null) return java.util.List.of();
        java.time.LocalDate now = java.time.LocalDate.now();
        return reminderService.getAllReminders().stream()
                .filter(r -> r.getStatus() == ReminderStatus.ACTIVE && r.getCar() != null && r.getCar().getOwner() != null && r.getCar().getOwner().getId().equals(userId))
                .map(r -> {
                    if (r.getDueDate() == null) return null;
                    long days = java.time.temporal.ChronoUnit.DAYS.between(now, r.getDueDate());
                    String type = r.getReminderType();
                    if (days < 0) return type + " overdue by " + Math.abs(days) + " days 🔴";
                    if (days == 0) return type + " is due today! 🚨";
                    if (days == 1) return type + " is due tomorrow 🟡";
                    if (days <= 7) return type + " due in " + days + " days 🟡";
                    return null; // Don't show alerts for far-off things
                })
                .filter(java.util.Objects::nonNull)
                .toList();
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
            java.util.List<ServiceReminder> userReminders = reminderService.getAllReminders().stream()
                    .filter(r -> r.getCar() != null && r.getCar().getOwner() != null && r.getCar().getOwner().getId().equals(userId))
                    .toList();
            model.addAttribute("reminders", userReminders);
            model.addAttribute("overdueCount", userReminders.stream()
                    .filter(r -> r.getStatus() == ReminderStatus.ACTIVE && r.getDueDate() != null && r.getDueDate().isBefore(java.time.LocalDate.now()))
                    .count());
        }
        return "reminders/list";
    }

    @GetMapping("/new")
    public String showForm(@CookieValue(name = "userRole", required = false) String role,
                           Model model) {
        if (!"SUPER_ADMIN".equals(role) && !"ADMIN".equals(role)) {
            return "redirect:/reminders";
        }
        model.addAttribute("reminder", new ServiceReminder());
        model.addAttribute("cars", carService.getAllCars());
        model.addAttribute("statuses", ReminderStatus.values());
        model.addAttribute("userRole", role);
        return "reminders/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute ServiceReminder reminder, 
                       @RequestParam(required = false) Long carId,
                       @CookieValue(name = "userRole", required = false) String role) {
        if (!"SUPER_ADMIN".equals(role) && !"ADMIN".equals(role)) {
            return "redirect:/reminders";
        }
        if (carId != null) carService.getCarById(carId).ifPresent(reminder::setCar);
        reminderService.saveReminder(reminder);
        if ("SUPER_ADMIN".equals(role) || "ADMIN".equals(role)) {
            return "redirect:/admin-dashboard";
        }
        return "redirect:/dashboard";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, 
                       @CookieValue(name = "userRole", required = false) String role,
                       Model model) {
        if (!"SUPER_ADMIN".equals(role) && !"ADMIN".equals(role)) {
            return "redirect:/reminders";
        }
        model.addAttribute("reminder", reminderService.getReminderById(id).orElseThrow());
        model.addAttribute("cars", carService.getAllCars());
        model.addAttribute("statuses", ReminderStatus.values());
        model.addAttribute("userRole", role);
        return "reminders/form";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable Long id, @ModelAttribute ServiceReminder reminder,
                         @RequestParam(required = false) Long carId,
                         @CookieValue(name = "userRole", required = false) String role) {
        if (!"SUPER_ADMIN".equals(role) && !"ADMIN".equals(role)) {
            return "redirect:/reminders";
        }
        if (carId != null) carService.getCarById(carId).ifPresent(reminder::setCar);
        reminderService.updateReminder(id, reminder);
        if ("SUPER_ADMIN".equals(role) || "ADMIN".equals(role)) {
            return "redirect:/admin-dashboard";
        }
        return "redirect:/dashboard";
    }

    @GetMapping("/{id}/dismiss")
    public String dismiss(@PathVariable Long id,
                          @CookieValue(name = "userId", required = false) Long userId,
                          @CookieValue(name = "userRole", required = false) String role) {
        ServiceReminder reminder = reminderService.getReminderById(id).orElse(null);
        if (reminder != null) {
            // Ownership check
            if (!"SUPER_ADMIN".equals(role) && !"ADMIN".equals(role)) {
                if (reminder.getCar() == null || reminder.getCar().getOwner() == null || !reminder.getCar().getOwner().getId().equals(userId)) {
                    return "redirect:/reminders";
                }
            }
            reminderService.dismissReminder(id);
        }
        if ("SUPER_ADMIN".equals(role) || "ADMIN".equals(role)) {
            return "redirect:/admin-dashboard";
        }
        return "redirect:/dashboard";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id,
                         @CookieValue(name = "userId", required = false) Long userId,
                         @CookieValue(name = "userRole", required = false) String role) {
        if (!"SUPER_ADMIN".equals(role) && !"ADMIN".equals(role)) {
            ServiceReminder reminder = reminderService.getReminderById(id).orElse(null);
            if (reminder == null || reminder.getCar() == null || reminder.getCar().getOwner() == null || !reminder.getCar().getOwner().getId().equals(userId)) {
                return "redirect:/reminders";
            }
        }
        reminderService.deleteReminder(id);
        if ("SUPER_ADMIN".equals(role) || "ADMIN".equals(role)) {
            return "redirect:/admin-dashboard";
        }
        return "redirect:/dashboard";
    }
}
