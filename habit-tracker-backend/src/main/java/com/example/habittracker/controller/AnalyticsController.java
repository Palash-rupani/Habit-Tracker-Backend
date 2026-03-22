package com.example.habittracker.controller;

import com.example.habittracker.dto.HabitAnalyticsResponse;
import com.example.habittracker.dto.LeaderboardEntry;
import com.example.habittracker.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analytics")
@CrossOrigin(origins = "*")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    private String getLoggedInEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    // GET analytics for a specific habit
    @GetMapping("/habits/{id}")
    public ResponseEntity<HabitAnalyticsResponse> getHabitAnalytics(@PathVariable Long id) {
        return ResponseEntity.ok(analyticsService.getHabitAnalytics(id));
    }

    // GET analytics for all habits of logged-in user
    @GetMapping("/me")
    public ResponseEntity<List<HabitAnalyticsResponse>> getUserAnalytics() {
        String email = getLoggedInEmail();
        return ResponseEntity.ok(analyticsService.getUserAnalytics(email));
    }

    // GET group leaderboard
    @GetMapping("/groups/{id}/leaderboard")
    public ResponseEntity<List<LeaderboardEntry>> getGroupLeaderboard(@PathVariable Long id) {
        return ResponseEntity.ok(analyticsService.getGroupLeaderboard(id));
    }
}