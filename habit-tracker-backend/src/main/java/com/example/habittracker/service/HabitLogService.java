package com.example.habittracker.service;
import com.example.habittracker.exception.ResourceNotFoundException;

import com.example.habittracker.dto.HabitLogResponse;
import com.example.habittracker.entity.Habit;
import com.example.habittracker.entity.HabitLog;
import com.example.habittracker.repository.HabitLogRepository;
import com.example.habittracker.repository.HabitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HabitLogService {

    @Autowired
    private HabitLogRepository habitLogRepository;

    @Autowired
    private HabitRepository habitRepository;

    // Convert entity to response DTO
    private HabitLogResponse toResponse(HabitLog log) {
        return new HabitLogResponse(
                log.getId(),
                log.getHabit().getId(),
                log.getHabit().getName(),
                log.getUserEmail(),
                log.getCheckInDate(),
                log.getCreatedAt()
        );
    }

    // Daily check-in
    public HabitLogResponse checkIn(Long habitId, String userEmail) {
        Habit habit = habitRepository.findById(habitId)
                .orElseThrow(() -> new ResourceNotFoundException("Habit not found"));

        habitLogRepository.findByHabitIdAndCheckInDate(habitId, LocalDate.now())
                .ifPresent(log -> {
                    throw new RuntimeException("Already checked in today for this habit");
                });

        HabitLog log = new HabitLog();
        log.setHabit(habit);
        log.setUserEmail(userEmail);
        log.setCheckInDate(LocalDate.now());

        return toResponse(habitLogRepository.save(log));
    }

// Get all logs for a habit
public List<HabitLogResponse> getLogs(Long habitId) {
    // Check if habit exists first
    habitRepository.findById(habitId)
            .orElseThrow(() -> new ResourceNotFoundException("Habit not found"));

    return habitLogRepository.findByHabitIdOrderByCheckInDateAsc(habitId)
            .stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
}

    // Calculate current streak
    public int getCurrentStreak(Long habitId) {
        List<HabitLog> logs = habitLogRepository
                .findByHabitIdOrderByCheckInDateAsc(habitId);

        if (logs.isEmpty()) return 0;

        int streak = 0;
        LocalDate today = LocalDate.now();

        for (int i = logs.size() - 1; i >= 0; i--) {
            LocalDate logDate = logs.get(i).getCheckInDate();
            if (logDate.equals(today.minusDays(streak))) {
                streak++;
            } else {
                break;
            }
        }

        return streak;
    }

    // Calculate longest streak
    public int getLongestStreak(Long habitId) {
        List<HabitLog> logs = habitLogRepository
                .findByHabitIdOrderByCheckInDateAsc(habitId);

        if (logs.isEmpty()) return 0;

        int longest = 1;
        int current = 1;

        for (int i = 1; i < logs.size(); i++) {
            LocalDate prev = logs.get(i - 1).getCheckInDate();
            LocalDate curr = logs.get(i).getCheckInDate();

            if (curr.equals(prev.plusDays(1))) {
                current++;
                longest = Math.max(longest, current);
            } else {
                current = 1;
            }
        }

        return longest;
    }
}