package com.example.usermanagement.repository;

import com.example.usermanagement.model.ServiceRecord;
import com.example.usermanagement.model.ServiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ServiceRecordRepository extends JpaRepository<ServiceRecord, Long> {
    List<ServiceRecord> findByCar_Id(Long carId);
    List<ServiceRecord> findByStatus(ServiceStatus status);
}
