package com.example.habittracker.dto;

public class HabitAnalyticsResponse {
    private Long habitId;
    private String habitName;
    private int totalCheckIns;
    private int currentStreak;
    private int longestStreak;
    private double completionRate; // percentage

    public HabitAnalyticsResponse(Long habitId, String habitName,
                                   int totalCheckIns, int currentStreak,
                                   int longestStreak, double completionRate) {
        this.habitId = habitId;
        this.habitName = habitName;
        this.totalCheckIns = totalCheckIns;
        this.currentStreak = currentStreak;
        this.longestStreak = longestStreak;
        this.completionRate = completionRate;
    }

    public Long getHabitId() { return habitId; }
    public String getHabitName() { return habitName; }
    public int getTotalCheckIns() { return totalCheckIns; }
    public int getCurrentStreak() { return currentStreak; }
    public int getLongestStreak() { return longestStreak; }
    public double getCompletionRate() { return completionRate; }
}