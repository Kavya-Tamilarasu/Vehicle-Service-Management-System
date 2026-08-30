package com.example.usermanagement.controller;

import com.example.usermanagement.model.ServiceRecord;
import com.example.usermanagement.model.ServiceStatus;
import com.example.usermanagement.service.CarService;
import com.example.usermanagement.service.ServiceRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/service-records")
public class ServiceRecordController {

    @Autowired private ServiceRecordService recordService;
    @Autowired private CarService carService;

    @GetMapping("/api")
    @ResponseBody
    public java.util.List<ServiceRecord> apiList(@RequestParam(required = false) Long carId) {
        if (carId != null) return recordService.getRecordsByCarId(carId);
        return recordService.getAllRecords();
    }

    @GetMapping
    public String list(@RequestParam(required = false) Long carId,
                       @CookieValue(name = "userId", required = false) Long userId,
                       @CookieValue(name = "userRole", required = false) String role,
                       Model model) {
        if ("SUPER_ADMIN".equals(role) || "ADMIN".equals(role)) {
            return "redirect:/admin-dashboard";
        }
        model.addAttribute("userRole", role);
        
        java.util.List<ServiceRecord> myRecords = new java.util.ArrayList<>();
        if (userId != null) {
            myRecords = recordService.getAllRecords().stream()
                    .filter(r -> r.getCar() != null && r.getCar().getOwner() != null && r.getCar().getOwner().getId().equals(userId))
                    .filter(r -> carId == null || (r.getCar() != null && r.getCar().getId().equals(carId)))
                    .toList();
        }
        model.addAttribute("records", myRecords);
        return "service-records/list";
    }

    @GetMapping("/new")
    public String showForm(@CookieValue(name = "userRole", required = false) String role,
                           @CookieValue(name = "userId", required = false) Long userId,
                           Model model) {
        model.addAttribute("record", new ServiceRecord());
        model.addAttribute("statuses", ServiceStatus.values());
        model.addAttribute("userRole", role);
        
        if ("SUPER_ADMIN".equals(role) || "ADMIN".equals(role)) {
            model.addAttribute("cars", carService.getAllCars());
        } else {
            // Customer can only see their own cars
            model.addAttribute("cars", carService.getAllCars().stream()
                    .filter(c -> c.getOwner() != null && c.getOwner().getId().equals(userId))
                    .toList());
        }
        return "service-records/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute ServiceRecord record, 
                       @RequestParam(required = false) Long carId,
                       @RequestParam(value = "invoiceFile", required = false) MultipartFile invoiceFile,
                       @CookieValue(name = "userId", required = false) Long userId,
                       @CookieValue(name = "userRole", required = false) String role) throws IOException {
        
        if (carId != null) {
            com.example.usermanagement.model.Car car = carService.getCarById(carId).orElse(null);
            if (car != null) {
                // If not admin, check if car belongs to user
                if (!"SUPER_ADMIN".equals(role) && !"ADMIN".equals(role)) {
                    if (car.getOwner() == null || !car.getOwner().getId().equals(userId)) {
                        return "redirect:/service-records";
                    }
                }
                record.setCar(car);
            }
        }
        
        if (!"SUPER_ADMIN".equals(role) && !"ADMIN".equals(role)) {
            record.setStatus(ServiceStatus.PENDING);
            record.setInvoicePath(null);
        } else {
            if (invoiceFile != null && !invoiceFile.isEmpty()) {
                String fileName = recordService.saveInvoice(invoiceFile);
                record.setInvoicePath(fileName);
            }
        }
        
        recordService.saveRecord(record);
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
            return "redirect:/service-records";
        }
        model.addAttribute("record", recordService.getRecordById(id).orElseThrow());
        model.addAttribute("cars", carService.getAllCars());
        model.addAttribute("statuses", ServiceStatus.values());
        model.addAttribute("userRole", role);
        return "service-records/form";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable Long id, @ModelAttribute ServiceRecord record,
                         @RequestParam(required = false) Long carId,
                         @RequestParam(value = "invoiceFile", required = false) MultipartFile invoiceFile,
                         @CookieValue(name = "userRole", required = false) String role) throws IOException {
        if (!"SUPER_ADMIN".equals(role) && !"ADMIN".equals(role)) {
            return "redirect:/service-records";
        }

        ServiceRecord existing = recordService.getRecordById(id).orElseThrow();
        existing.setServiceType(record.getServiceType());
        existing.setDescription(record.getDescription());
        existing.setDate(record.getDate());
        existing.setCost(record.getCost());
        existing.setStatus(record.getStatus());
        if (record.getPaymentStatus() != null) existing.setPaymentStatus(record.getPaymentStatus());
        if (record.getPaymentMethod() != null) existing.setPaymentMethod(record.getPaymentMethod());

        if (carId != null) carService.getCarById(carId).ifPresent(existing::setCar);
        
        if (invoiceFile != null && !invoiceFile.isEmpty()) {
            String fileName = recordService.saveInvoice(invoiceFile);
            existing.setInvoicePath(fileName);
        }
        
        recordService.saveRecord(existing);
        
        if ("SUPER_ADMIN".equals(role) || "ADMIN".equals(role)) {
            return "redirect:/admin-dashboard";
        }
        return "redirect:/dashboard";
    }

    @GetMapping("/approve-request/{id}")
    public String approve(@PathVariable("id") Long id,
                          @CookieValue(name = "userRole", required = false) String role) {
        if (!"SUPER_ADMIN".equals(role) && !"ADMIN".equals(role)) {
            return "redirect:/login";
        }
        recordService.getRecordById(id).ifPresent(r -> {
            r.setStatus(ServiceStatus.APPROVED);
            recordService.saveRecord(r);
        });
        return "redirect:/admin-dashboard";
    }

    @GetMapping("/status/reject/{id}")
    public String reject(@PathVariable("id") Long id,
                         @CookieValue(name = "userRole", required = false) String role) {
        if (!"SUPER_ADMIN".equals(role) && !"ADMIN".equals(role)) {
            return "redirect:/service-records";
        }
        recordService.getRecordById(id).ifPresent(r -> {
            r.setStatus(ServiceStatus.REJECTED);
            recordService.saveRecord(r);
        });
        return "redirect:/admin-dashboard";
    }

    @GetMapping("/{id}/update-status")
    public String updateStatus(@PathVariable("id") Long id,
                               @RequestParam("status") String status,
                               @CookieValue(name = "userRole", required = false) String role) {
        if (!"SUPER_ADMIN".equals(role) && !"ADMIN".equals(role)) {
            return "redirect:/login";
        }
        recordService.getRecordById(id).ifPresent(r -> {
            try {
                r.setStatus(ServiceStatus.valueOf(status.toUpperCase()));
                recordService.saveRecord(r);
            } catch (Exception e) {
                // Invalid status
            }
        });
        return "redirect:/admin-dashboard";
    }

    @GetMapping("/{id}/complete")
    public String markComplete(@PathVariable("id") Long id,
                               @CookieValue(name = "userRole", required = false) String role) {
        if (!"SUPER_ADMIN".equals(role) && !"ADMIN".equals(role)) {
            return "redirect:/service-records";
        }
        recordService.markCompleted(id);
        return "redirect:/admin-dashboard";
    }

    @PostMapping("/{id}/update-payment")
    public String updatePayment(@PathVariable("id") Long id,
                                @RequestParam("cost") Double cost,
                                @RequestParam("paymentStatus") String paymentStatus,
                                @CookieValue(name = "userRole", required = false) String role) {
        if (!"SUPER_ADMIN".equals(role) && !"ADMIN".equals(role)) {
            return "redirect:/login";
        }
        recordService.getRecordById(id).ifPresent(r -> {
            r.setCost(cost);
            try {
                r.setPaymentStatus(com.example.usermanagement.model.PaymentStatus.valueOf(paymentStatus.toUpperCase()));
            } catch (Exception e) {
                // Keep existing status if invalid
            }
            recordService.saveRecord(r);
        });
        return "redirect:/admin-dashboard";
    }


    @GetMapping("/{id}/pay")
    public String processPayment(@PathVariable("id") Long id,
                                 @RequestParam String method,
                                 @CookieValue(name = "userId", required = false) Long userId,
                                 @CookieValue(name = "userRole", required = false) String role) {
        ServiceRecord r = recordService.getRecordById(id).orElse(null);
        if (r == null) return "redirect:/cars";
        
        // Ownership check
        if (!"SUPER_ADMIN".equals(role) && !"ADMIN".equals(role)) {
            if (r.getCar() == null || r.getCar().getOwner() == null || !r.getCar().getOwner().getId().equals(userId)) {
                return "redirect:/cars";
            }
        }

        r.setPaymentMethod(method);
        if ("CASH".equals(method)) {
            r.setPaymentStatus(com.example.usermanagement.model.PaymentStatus.CASH_ON_DELIVERY);
        } else {
            r.setPaymentStatus(com.example.usermanagement.model.PaymentStatus.PAID);
        }
        recordService.saveRecord(r);
        
        if ("SUPER_ADMIN".equals(role) || "ADMIN".equals(role)) {
            return "redirect:/admin-dashboard";
        }
        return "redirect:/cars";
    }

    @GetMapping("/{id}/cancel")
    public String cancel(@PathVariable("id") Long id,
                         @CookieValue(name = "userId", required = false) Long userId,
                         @CookieValue(name = "userRole", required = false) String role) {
        if (role == null) return "redirect:/login";
        
        ServiceRecord r = recordService.getRecordById(id).orElse(null);
        if (r != null) {
            // Ownership check
            if (!"SUPER_ADMIN".equals(role) && !"ADMIN".equals(role)) {
                if (r.getCar() == null || r.getCar().getOwner() == null || !r.getCar().getOwner().getId().equals(userId)) {
                    return "redirect:/service-records";
                }
            }
            r.setStatus(ServiceStatus.CANCELLED);
            recordService.saveRecord(r);
        }

        if ("SUPER_ADMIN".equals(role) || "ADMIN".equals(role)) {
            return "redirect:/admin-dashboard";
        }
        return "redirect:/dashboard";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id,
                         @CookieValue(name = "userId", required = false) Long userId,
                         @CookieValue(name = "userRole", required = false) String role) {
        if (role == null) return "redirect:/login";
        
        ServiceRecord r = recordService.getRecordById(id).orElse(null);
        if (r != null) {
            // Ownership check
            if (!"SUPER_ADMIN".equals(role) && !"ADMIN".equals(role)) {
                if (r.getCar() == null || r.getCar().getOwner() == null || !r.getCar().getOwner().getId().equals(userId)) {
                    return "redirect:/service-records";
                }
            }
            recordService.deleteRecord(id);
        }

        if ("SUPER_ADMIN".equals(role) || "ADMIN".equals(role)) {
            return "redirect:/admin-dashboard";
        }
        return "redirect:/dashboard";
    }

    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity<Resource> downloadInvoice(@PathVariable String fileName,
                                                   @RequestParam(defaultValue = "false") boolean download) {
        try {
            Path filePath = Paths.get("uploads", "invoices").resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            
            if (resource.exists() && resource.isReadable()) {
                String disposition = download ? "attachment" : "inline";
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_PDF)
                        .header(HttpHeaders.CONTENT_DISPOSITION, disposition + "; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
