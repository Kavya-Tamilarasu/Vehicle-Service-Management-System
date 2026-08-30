package com.example.usermanagement.repository;

import com.example.usermanagement.model.ReminderStatus;
import com.example.usermanagement.model.ServiceReminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ServiceReminderRepository extends JpaRepository<ServiceReminder, Long> {
    List<ServiceReminder> findByCar_Id(Long carId);
    List<ServiceReminder> findByStatus(ReminderStatus status);
}
