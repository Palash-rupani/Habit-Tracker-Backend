package com.example.habittracker.service;
import com.example.habittracker.exception.ResourceNotFoundException;

import com.example.habittracker.dto.HabitRequest;
import com.example.habittracker.dto.HabitResponse;
import com.example.habittracker.entity.Habit;
import com.example.habittracker.repository.HabitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HabitService {

    @Autowired
    private HabitRepository habitRepository;

    // Convert entity to response DTO
    private HabitResponse toResponse(Habit habit) {
        return new HabitResponse(
                habit.getId(),
                habit.getName(),
                habit.getDescription(),
                habit.getFrequency(),
                habit.getUserEmail(),
                habit.getCreatedAt(),
                habit.getUpdatedAt()
        );
    }

    // Create a new habit
    public HabitResponse createHabit(HabitRequest request, String userEmail) {
        Habit habit = new Habit();
        habit.setName(request.getName());
        habit.setDescription(request.getDescription());
        habit.setFrequency(request.getFrequency());
        habit.setUserEmail(userEmail);
        return toResponse(habitRepository.save(habit));
    }

    // Get all habits for the logged-in user
    public List<HabitResponse> getHabits(String userEmail) {
        return habitRepository.findByUserEmail(userEmail)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // Update an existing habit
    public HabitResponse updateHabit(Long id, HabitRequest request, String userEmail) {
        Habit habit = habitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Habit not found"));

        if (!habit.getUserEmail().equals(userEmail)) {
            throw new RuntimeException("Unauthorized");
        }

        habit.setName(request.getName());
        habit.setDescription(request.getDescription());
        habit.setFrequency(request.getFrequency());

        return toResponse(habitRepository.save(habit));
    }

    // Delete a habit
    public void deleteHabit(Long id, String userEmail) {
        Habit habit = habitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Habit not found"));

        if (!habit.getUserEmail().equals(userEmail)) {
            throw new RuntimeException("Unauthorized");
        }

        habitRepository.deleteById(id);
    }
}