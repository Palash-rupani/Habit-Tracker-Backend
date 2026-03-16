package com.example.habittracker.repository;

import com.example.habittracker.entity.Habit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HabitRepository extends JpaRepository<Habit, Long> {

    // fetch only the habits that belong to the logged-in user
    List<Habit> findByUserEmail(String userEmail);
}