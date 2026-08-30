package com.example.usermanagement.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "service_reminders")
public class ServiceReminder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;

    private String reminderType;
    private LocalDate dueDate;
    private String notes;

    @Enumerated(EnumType.STRING)
    private ReminderStatus status = ReminderStatus.ACTIVE;

    public ServiceReminder() {}

    public boolean isOverdue() {
        return status == ReminderStatus.ACTIVE && dueDate != null && dueDate.isBefore(LocalDate.now());
    }

    public boolean isDueSoon() {
        if (status != ReminderStatus.ACTIVE || dueDate == null) return false;
        LocalDate now = LocalDate.now();
        return !dueDate.isBefore(now) && dueDate.isBefore(now.plusDays(8));
    }

    public boolean isUpcoming() {
        if (status != ReminderStatus.ACTIVE || dueDate == null) return false;
        return dueDate.isAfter(LocalDate.now().plusDays(7));
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Car getCar() { return car; }
    public void setCar(Car car) { this.car = car; }

    public String getReminderType() { return reminderType; }
    public void setReminderType(String reminderType) { this.reminderType = reminderType; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public ReminderStatus getStatus() { return status; }
    public void setStatus(ReminderStatus status) { this.status = status; }
}
