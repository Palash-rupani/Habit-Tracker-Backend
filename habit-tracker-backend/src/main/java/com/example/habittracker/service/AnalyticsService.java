package com.example.habittracker.service;

import com.example.habittracker.dto.HabitAnalyticsResponse;
import com.example.habittracker.dto.LeaderboardEntry;
import com.example.habittracker.entity.HabitLog;
import com.example.habittracker.exception.ResourceNotFoundException;
import com.example.habittracker.repository.GroupMemberRepository;
import com.example.habittracker.repository.HabitLogRepository;
import com.example.habittracker.repository.HabitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

    @Autowired
    private HabitRepository habitRepository;

    @Autowired
    private HabitLogRepository habitLogRepository;

    @Autowired
    private GroupMemberRepository groupMemberRepository;

    // Get analytics for a specific habit
    public HabitAnalyticsResponse getHabitAnalytics(Long habitId) {
        var habit = habitRepository.findById(habitId)
                .orElseThrow(() -> new ResourceNotFoundException("Habit not found"));

        List<HabitLog> logs = habitLogRepository
                .findByHabitIdOrderByCheckInDateAsc(habitId);

        int totalCheckIns = logs.size();
        int currentStreak = calculateCurrentStreak(logs);
        int longestStreak = calculateLongestStreak(logs);
        double completionRate = calculateCompletionRate(logs, habit.getCreatedAt().toLocalDate());

        return new HabitAnalyticsResponse(
                habitId,
                habit.getName(),
                totalCheckIns,
                currentStreak,
                longestStreak,
                completionRate
        );
    }

    // Get analytics for all habits of a user
    public List<HabitAnalyticsResponse> getUserAnalytics(String userEmail) {
        return habitRepository.findByUserEmail(userEmail)
                .stream()
                .map(habit -> getHabitAnalytics(habit.getId()))
                .collect(Collectors.toList());
    }

    // Get group leaderboard
    public List<LeaderboardEntry> getGroupLeaderboard(Long groupId) {
        return groupMemberRepository.findByGroupId(groupId)
                .stream()
                .map(member -> {
                    List<HabitLog> logs = habitLogRepository
                            .findByUserEmailOrderByCheckInDateAsc(member.getUserEmail());

                    int currentStreak = calculateCurrentStreak(logs);
                    int longestStreak = calculateLongestStreak(logs);
                    int totalCheckIns = logs.size();

                    return new LeaderboardEntry(
                            member.getUserEmail(),
                            currentStreak,
                            totalCheckIns,
                            longestStreak
                    );
                })
                .sorted((a, b) -> b.getCurrentStreak() - a.getCurrentStreak())
                .collect(Collectors.toList());
    }

    // --- Helper methods ---

    private int calculateCurrentStreak(List<HabitLog> logs) {
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

    private int calculateLongestStreak(List<HabitLog> logs) {
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

    private double calculateCompletionRate(List<HabitLog> logs, LocalDate createdAt) {
        long totalDays = ChronoUnit.DAYS.between(createdAt, LocalDate.now()) + 1;
        if (totalDays <= 0) return 0;
        return Math.round((logs.size() * 100.0 / totalDays) * 10.0) / 10.0;
    }
}