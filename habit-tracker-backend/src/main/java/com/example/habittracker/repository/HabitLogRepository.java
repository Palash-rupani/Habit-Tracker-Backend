package com.example.habittracker.repository;

import com.example.habittracker.entity.HabitLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface HabitLogRepository extends JpaRepository<HabitLog, Long> {

    // Get all logs for a specific habit
    List<HabitLog> findByHabitIdOrderByCheckInDateAsc(Long habitId);

    // Check if user already checked in today for a habit
    Optional<HabitLog> findByHabitIdAndCheckInDate(Long habitId, LocalDate checkInDate);

    // Get all logs for a user
    List<HabitLog> findByUserEmailOrderByCheckInDateAsc(String userEmail);
}