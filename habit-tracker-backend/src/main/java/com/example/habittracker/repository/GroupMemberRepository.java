package com.example.habittracker.repository;

import com.example.habittracker.entity.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {

    // Get all members of a group
    List<GroupMember> findByGroupId(Long groupId);

    // Get all groups a user has joined
    List<GroupMember> findByUserEmail(String userEmail);

    // Check if a user is already a member of a group
    Optional<GroupMember> findByGroupIdAndUserEmail(Long groupId, String userEmail);
}