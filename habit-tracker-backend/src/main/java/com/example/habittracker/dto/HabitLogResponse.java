package com.example.habittracker.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class HabitLogResponse {
    private Long id;
    private Long habitId;
    private String habitName;
    private String userEmail;
    private LocalDate checkInDate;
    private LocalDateTime createdAt;

    public HabitLogResponse(Long id, Long habitId, String habitName,
                            String userEmail, LocalDate checkInDate,
                            LocalDateTime createdAt) {
        this.id = id;
        this.habitId = habitId;
        this.habitName = habitName;
        this.userEmail = userEmail;
        this.checkInDate = checkInDate;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public Long getHabitId() { return habitId; }
    public String getHabitName() { return habitName; }
    public String getUserEmail() { return userEmail; }
    public LocalDate getCheckInDate() { return checkInDate; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}