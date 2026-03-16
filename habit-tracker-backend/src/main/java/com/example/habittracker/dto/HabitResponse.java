package com.example.habittracker.dto;

import java.time.LocalDateTime;

public class HabitResponse {
    private Long id;
    private String name;
    private String description;
    private String frequency;
    private String userEmail;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public HabitResponse(Long id, String name, String description,
                         String frequency, String userEmail,
                         LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.frequency = frequency;
        this.userEmail = userEmail;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getFrequency() { return frequency; }
    public String getUserEmail() { return userEmail; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}