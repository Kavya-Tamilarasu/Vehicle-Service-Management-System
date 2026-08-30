package com.example.usermanagement.controller;

import com.example.usermanagement.model.SparePart;
import com.example.usermanagement.service.SparePartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@Controller
@RequestMapping("/spare-parts")
public class SparePartController {

    @Autowired private SparePartService sparePartService;
    @Autowired private com.example.usermanagement.repository.PartRequestRepository partRequestRepository;
    @Autowired private com.example.usermanagement.service.UserService userService;

    @PostMapping("/request")
    @ResponseBody
    public String handleRequest(@RequestParam String partName, 
                               @CookieValue(name = "userId", required = false) Long userId) {
        if (userId == null) return "Error: Not logged in";
        
        com.example.usermanagement.model.PartRequest request = new com.example.usermanagement.model.PartRequest();
        request.setPartName(partName);
        request.setRequestDate(java.time.LocalDateTime.now());
        request.setStatus("PENDING");
        userService.getUserById(userId).ifPresent(request::setUser);
        partRequestRepository.save(request);

        // Check if part is in stock
        boolean inStock = sparePartService.getAllParts().stream()
                .anyMatch(p -> p.getPartName().equalsIgnoreCase(partName) && p.getQuantityInStock() > 0);
        
        return inStock ? "AVAILABLE" : "UNAVAILABLE";
    }

    @PostMapping("/requests/{id}/fulfill")
    @ResponseBody
    public String fulfillRequest(@PathVariable Long id) {
        partRequestRepository.findById(id).ifPresent(req -> {
            req.setStatus("COMPLETED");
            partRequestRepository.save(req);
        });
        return "Success";
    }

    @GetMapping("/requests/api")
    @ResponseBody
    public java.util.List<com.example.usermanagement.model.PartRequest> getAllRequests() {
        return partRequestRepository.findAll();
    }

    @GetMapping("/api")
    @ResponseBody
    public java.util.List<com.example.usermanagement.model.SparePart> apiList() {
        return sparePartService.getAllParts();
    }

    @GetMapping
    public String list(@CookieValue(name = "userRole", required = false) String role,
                       @CookieValue(name = "userId", required = false) Long userId,
                       Model model) {
        if ("SUPER_ADMIN".equals(role) || "ADMIN".equals(role)) {
            return "redirect:/admin-dashboard";
        }
        model.addAttribute("userRole", role);
        model.addAttribute("parts", sparePartService.getAllParts());
        model.addAttribute("lowStockCount", sparePartService.getLowStockParts().size());
        
        if (userId != null) {
            model.addAttribute("myRequests", partRequestRepository.findByUserId(userId));
        }
        
        return "spare-parts/list";
    }

    @GetMapping("/new")
    public String showForm(@CookieValue(name = "userRole", required = false) String role,
                           Model model) {
        if (!"SUPER_ADMIN".equals(role) && !"ADMIN".equals(role)) {
            return "redirect:/spare-parts";
        }
        SparePart part = new SparePart();
        part.setDateAdded(LocalDate.now());
        model.addAttribute("part", part);
        model.addAttribute("userRole", role);
        return "spare-parts/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute SparePart part,
                       @CookieValue(name = "userRole", required = false) String role) {
        if (!"SUPER_ADMIN".equals(role) && !"ADMIN".equals(role)) {
            return "redirect:/spare-parts";
        }
        sparePartService.savePart(part);
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
            return "redirect:/spare-parts";
        }
        model.addAttribute("part", sparePartService.getPartById(id).orElseThrow());
        model.addAttribute("userRole", role);
        return "spare-parts/form";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable Long id, @ModelAttribute SparePart part,
                         @CookieValue(name = "userRole", required = false) String role) {
        if (!"SUPER_ADMIN".equals(role) && !"ADMIN".equals(role)) {
            return "redirect:/spare-parts";
        }
        sparePartService.updatePart(id, part);
        if ("SUPER_ADMIN".equals(role) || "ADMIN".equals(role)) {
            return "redirect:/admin-dashboard";
        }
        return "redirect:/dashboard";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id,
                         @CookieValue(name = "userRole", required = false) String role) {
        if (!"SUPER_ADMIN".equals(role) && !"ADMIN".equals(role)) {
            return "redirect:/spare-parts";
        }
        sparePartService.deletePart(id);
        if ("SUPER_ADMIN".equals(role) || "ADMIN".equals(role)) {
            return "redirect:/admin-dashboard";
        }
        return "redirect:/dashboard";
    }
}
