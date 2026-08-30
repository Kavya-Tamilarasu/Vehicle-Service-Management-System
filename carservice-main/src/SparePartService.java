package com.example.usermanagement.service;

import com.example.usermanagement.model.SparePart;
import com.example.usermanagement.repository.SparePartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class SparePartService {

    @Autowired
    private SparePartRepository repo;

    public List<SparePart> getAllParts() { return repo.findAll(); }

    public Optional<SparePart> getPartById(Long id) { return repo.findById(id); }

    public List<SparePart> getLowStockParts() { return repo.findLowStockParts(); }

    public List<SparePart> searchByName(String name) { return repo.findByPartNameContainingIgnoreCase(name); }

    public SparePart savePart(SparePart part) { return repo.save(part); }

    public SparePart updatePart(Long id, SparePart updated) {
        SparePart part = repo.findById(id).orElseThrow();
        part.setPartName(updated.getPartName());
        part.setCategory(updated.getCategory());
        part.setBrand(updated.getBrand());
        part.setQuantityInStock(updated.getQuantityInStock());
        part.setMinimumStockLevel(updated.getMinimumStockLevel());
        part.setUnitPrice(updated.getUnitPrice());
        part.setSupplier(updated.getSupplier());
        part.setDateAdded(updated.getDateAdded());
        return repo.save(part);
    }

    public void deletePart(Long id) { repo.deleteById(id); }
}
