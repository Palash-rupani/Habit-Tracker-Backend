package com.example.habittracker.controller;

import com.example.habittracker.dto.HabitRequest;
import com.example.habittracker.dto.HabitResponse;
import com.example.habittracker.service.HabitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/habits")
@CrossOrigin(origins = "*")
public class HabitController {

    @Autowired
    private HabitService habitService;

    private String getLoggedInEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    // CREATE a habit
    @PostMapping
    public ResponseEntity<HabitResponse> createHabit(@RequestBody HabitRequest request) {
        String email = getLoggedInEmail();
        HabitResponse response = habitService.createHabit(request, email);
        return ResponseEntity.ok(response);
    }

    // GET all habits for logged-in user
    @GetMapping
    public ResponseEntity<List<HabitResponse>> getHabits() {
        String email = getLoggedInEmail();
        List<HabitResponse> habits = habitService.getHabits(email);
        return ResponseEntity.ok(habits);
    }

    // UPDATE a habit
    @PutMapping("/{id}")
    public ResponseEntity<HabitResponse> updateHabit(@PathVariable Long id,
                                                      @RequestBody HabitRequest request) {
        String email = getLoggedInEmail();
        HabitResponse updated = habitService.updateHabit(id, request, email);
        return ResponseEntity.ok(updated);
    }

    // DELETE a habit
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteHabit(@PathVariable Long id) {
        String email = getLoggedInEmail();
        habitService.deleteHabit(id, email);
        return ResponseEntity.ok("Habit deleted successfully");
    }
}