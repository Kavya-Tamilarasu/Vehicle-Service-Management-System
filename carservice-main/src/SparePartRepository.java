package com.example.usermanagement.repository;

import com.example.usermanagement.model.SparePart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SparePartRepository extends JpaRepository<SparePart, Long> {
    List<SparePart> findByCategory(String category);

    @Query("SELECT s FROM SparePart s WHERE s.quantityInStock <= s.minimumStockLevel")
    List<SparePart> findLowStockParts();

    List<SparePart> findByPartNameContainingIgnoreCase(String name);
}
