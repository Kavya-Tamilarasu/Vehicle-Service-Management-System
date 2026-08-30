package com.example.usermanagement.service;

import com.example.usermanagement.model.ServiceRecord;
import com.example.usermanagement.model.ServiceStatus;
import com.example.usermanagement.repository.ServiceRecordRepository;
import com.example.usermanagement.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@org.springframework.transaction.annotation.Transactional
public class ServiceRecordService {

    @Autowired
    private ServiceRecordRepository repo;

    @Autowired
    private ReviewRepository reviewRepo;

    private final String uploadDir = "uploads/invoices/";

    public List<ServiceRecord> getAllRecords() { return repo.findAll(); }

    public Optional<ServiceRecord> getRecordById(Long id) { return repo.findById(id); }

    public List<ServiceRecord> getRecordsByCarId(Long carId) { return repo.findByCar_Id(carId); }

    public ServiceRecord saveRecord(ServiceRecord r) { return repo.save(r); }

    public ServiceRecord updateRecord(Long id, ServiceRecord updated) {
        ServiceRecord r = repo.findById(id).orElseThrow();
        if (updated.getCar() != null) r.setCar(updated.getCar());
        r.setServiceType(updated.getServiceType());
        r.setDescription(updated.getDescription());
        r.setDate(updated.getDate());
        r.setCost(updated.getCost());
        r.setStatus(updated.getStatus());
        if (updated.getInvoicePath() != null) {
            r.setInvoicePath(updated.getInvoicePath());
        }
        return repo.save(r);
    }

    public ServiceRecord markCompleted(Long id) {
        ServiceRecord r = repo.findById(id).orElseThrow();
        r.setStatus(ServiceStatus.COMPLETED);
        return repo.save(r);
    }

    public void deleteRecord(Long id) { 
        // Efficiently delete associated reviews first to satisfy foreign key constraints
        reviewRepo.deleteByServiceRecord_Id(id);
        repo.deleteById(id); 
    }

    public String saveInvoice(MultipartFile file) throws IOException {
        if (file.isEmpty()) return null;
        
        Path root = Paths.get(uploadDir);
        if (!Files.exists(root)) {
            Files.createDirectories(root);
        }
        
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path targetPath = root.resolve(fileName);
        Files.copy(file.getInputStream(), targetPath);
        
        return fileName; // Return just the file name
    }
}
