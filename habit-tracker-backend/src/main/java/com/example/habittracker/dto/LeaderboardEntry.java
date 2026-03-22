package com.example.habittracker.dto;

public class LeaderboardEntry {
    private String userEmail;
    private int currentStreak;
    private int totalCheckIns;
    private int longestStreak;

    public LeaderboardEntry(String userEmail, int currentStreak,
                            int totalCheckIns, int longestStreak) {
        this.userEmail = userEmail;
        this.currentStreak = currentStreak;
        this.totalCheckIns = totalCheckIns;
        this.longestStreak = longestStreak;
    }

    public String getUserEmail() { return userEmail; }
    public int getCurrentStreak() { return currentStreak; }
    public int getTotalCheckIns() { return totalCheckIns; }
    public int getLongestStreak() { return longestStreak; }
}