package com.example.usermanagement.controller;

import com.example.usermanagement.model.Car;
import com.example.usermanagement.service.CarService;
import com.example.usermanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.usermanagement.model.ServiceStatus;

@Controller
@RequestMapping("/cars")
public class CarController {

    @Autowired
    private CarService carService;
    @Autowired
    private UserService userService;
    @Autowired
    private com.example.usermanagement.service.ServiceRecordService recordService;

    @GetMapping("/api")
    @ResponseBody
    public java.util.List<Car> apiList() {
        return carService.getAllCars();
    }

    @GetMapping
    public String list(@CookieValue(name = "userId", required = false) Long userId,
            @CookieValue(name = "userRole", required = false) String role,
            Model model) {
        if ("SUPER_ADMIN".equals(role) || "ADMIN".equals(role)) {
            return "redirect:/admin-dashboard";
        }
        model.addAttribute("userRole", role);

        java.util.List<Car> myCars = new java.util.ArrayList<>();
        java.util.List<com.example.usermanagement.model.ServiceRecord> myBookings = new java.util.ArrayList<>();

        if (userId != null) {
            myCars = carService.getCarsByUserId(userId);

            // Fetch all non-completed bookings for these cars (Pending, Approved, Rejected)
            myBookings = recordService.getAllRecords().stream()
                    .filter(r -> r.getCar() != null && r.getCar().getOwner() != null
                            && r.getCar().getOwner().getId().equals(userId))
                    .filter(r -> r.getStatus() != ServiceStatus.COMPLETED)
                    .toList();
        }

        model.addAttribute("cars", myCars);
        model.addAttribute("bookings", myBookings);
        return "cars/list";
    }

    @GetMapping("/new")
    public String showForm(@CookieValue(name = "userRole", required = false) String role,
            @CookieValue(name = "userId", required = false) Long userId,
            Model model) {
        model.addAttribute("car", new Car());
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("userRole", role);
        model.addAttribute("userId", userId);
        return "cars/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Car car,
            @RequestParam(required = false) Long ownerId,
            @CookieValue(name = "userId", required = false) Long userId,
            @CookieValue(name = "userRole", required = false) String role) {
        
        Long effectiveOwnerId = ownerId;
        if (!"SUPER_ADMIN".equals(role) && !"ADMIN".equals(role)) {
            effectiveOwnerId = userId;
        }

        if (effectiveOwnerId != null)
            userService.getUserById(effectiveOwnerId).ifPresent(car::setOwner);
            
        carService.saveCar(car);
        
        if ("SUPER_ADMIN".equals(role) || "ADMIN".equals(role)) {
            return "redirect:/admin-dashboard";
        }
        return "redirect:/dashboard";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id,
            @CookieValue(name = "userId", required = false) Long userId,
            @CookieValue(name = "userRole", required = false) String role,
            Model model) {
        Car car = carService.getCarById(id).orElseThrow();
        
        // Allow if admin or if current user is the owner
        if (!"SUPER_ADMIN".equals(role) && !"ADMIN".equals(role)) {
            if (car.getOwner() == null || !car.getOwner().getId().equals(userId)) {
                return "redirect:/cars";
            }
        }
        
        model.addAttribute("car", car);
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("userRole", role);
        model.addAttribute("userId", userId);
        return "cars/form";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable Long id, @ModelAttribute Car car,
            @RequestParam(required = false) Long ownerId,
            @CookieValue(name = "userId", required = false) Long userId,
            @CookieValue(name = "userRole", required = false) String role) {
        
        Car existing = carService.getCarById(id).orElseThrow();
        if (!"SUPER_ADMIN".equals(role) && !"ADMIN".equals(role)) {
            if (existing.getOwner() == null || !existing.getOwner().getId().equals(userId)) {
                return "redirect:/cars";
            }
            car.setOwner(existing.getOwner()); // Keep original owner
        } else {
            if (ownerId != null)
                userService.getUserById(ownerId).ifPresent(car::setOwner);
        }

        carService.updateCar(id, car);
        if ("SUPER_ADMIN".equals(role) || "ADMIN".equals(role)) {
            return "redirect:/admin-dashboard";
        }
        return "redirect:/dashboard";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id,
            @CookieValue(name = "userId", required = false) Long userId,
            @CookieValue(name = "userRole", required = false) String role) {
        
        Car car = carService.getCarById(id).orElseThrow();
        if (!"SUPER_ADMIN".equals(role) && !"ADMIN".equals(role)) {
            if (car.getOwner() == null || !car.getOwner().getId().equals(userId)) {
                return "redirect:/cars";
            }
        }

        carService.deleteCar(id);
        if ("SUPER_ADMIN".equals(role) || "ADMIN".equals(role)) {
            return "redirect:/admin-dashboard";
        }
        return "redirect:/dashboard";
    }
}
