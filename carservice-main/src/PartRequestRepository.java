package com.example.usermanagement.repository;

import com.example.usermanagement.model.PartRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartRequestRepository extends JpaRepository<PartRequest, Long> {
    List<PartRequest> findByUserId(Long userId);
}
