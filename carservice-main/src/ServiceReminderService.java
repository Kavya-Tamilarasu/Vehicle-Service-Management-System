package com.example.usermanagement.service;

import com.example.usermanagement.model.ReminderStatus;
import com.example.usermanagement.model.ServiceReminder;
import com.example.usermanagement.repository.ServiceReminderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ServiceReminderService {

    @Autowired
    private ServiceReminderRepository repo;

    public List<ServiceReminder> getAllReminders() { return repo.findAll(); }

    public Optional<ServiceReminder> getReminderById(Long id) { return repo.findById(id); }

    public List<ServiceReminder> getRemindersByCarId(Long carId) { return repo.findByCar_Id(carId); }

    public List<ServiceReminder> getActiveReminders() { return repo.findByStatus(ReminderStatus.ACTIVE); }

    public List<ServiceReminder> getOverdueReminders() {
        return repo.findByStatus(ReminderStatus.ACTIVE).stream()
                .filter(r -> r.getDueDate() != null && r.getDueDate().isBefore(LocalDate.now()))
                .collect(Collectors.toList());
    }

    public ServiceReminder saveReminder(ServiceReminder r) { return repo.save(r); }

    public ServiceReminder updateReminder(Long id, ServiceReminder updated) {
        ServiceReminder r = repo.findById(id).orElseThrow();
        r.setCar(updated.getCar());
        r.setReminderType(updated.getReminderType());
        r.setDueDate(updated.getDueDate());
        r.setNotes(updated.getNotes());
        r.setStatus(updated.getStatus());
        return repo.save(r);
    }

    public ServiceReminder dismissReminder(Long id) {
        ServiceReminder r = repo.findById(id).orElseThrow();
        r.setStatus(ReminderStatus.DISMISSED);
        return repo.save(r);
    }

    public void deleteReminder(Long id) { repo.deleteById(id); }
}
