package com.example.habittracker.repository;

import com.example.habittracker.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    // Get all groups created by a user
    List<Group> findByCreatedBy(String createdBy);
}