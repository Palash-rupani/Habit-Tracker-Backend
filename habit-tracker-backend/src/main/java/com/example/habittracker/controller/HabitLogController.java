package com.example.habittracker.controller;

import com.example.habittracker.dto.HabitLogResponse;
import com.example.habittracker.service.HabitLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/habits")
@CrossOrigin(origins = "*")
public class HabitLogController {

    @Autowired
    private HabitLogService habitLogService;

    private String getLoggedInEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    // Daily check-in
    @PostMapping("/{id}/checkin")
    public ResponseEntity<HabitLogResponse> checkIn(@PathVariable Long id) {
        String email = getLoggedInEmail();
        HabitLogResponse log = habitLogService.checkIn(id, email);
        return ResponseEntity.ok(log);
    }

    // Get all logs for a habit
    @GetMapping("/{id}/logs")
    public ResponseEntity<List<HabitLogResponse>> getLogs(@PathVariable Long id) {
        List<HabitLogResponse> logs = habitLogService.getLogs(id);
        return ResponseEntity.ok(logs);
    }

    // Get streaks for a habit
    @GetMapping("/{id}/streaks")
    public ResponseEntity<Map<String, Integer>> getStreaks(@PathVariable Long id) {
        Map<String, Integer> streaks = new HashMap<>();
        streaks.put("currentStreak", habitLogService.getCurrentStreak(id));
        streaks.put("longestStreak", habitLogService.getLongestStreak(id));
        return ResponseEntity.ok(streaks);
    }
}