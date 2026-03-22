package com.example.habittracker.service;

import com.example.habittracker.dto.GroupMemberResponse;
import com.example.habittracker.dto.GroupRequest;
import com.example.habittracker.dto.GroupResponse;
import com.example.habittracker.entity.Group;
import com.example.habittracker.entity.GroupMember;
import com.example.habittracker.exception.ResourceNotFoundException;
import com.example.habittracker.repository.GroupMemberRepository;
import com.example.habittracker.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupMemberRepository groupMemberRepository;

    // Convert Group entity to GroupResponse DTO
    private GroupResponse toGroupResponse(Group group) {
        return new GroupResponse(
                group.getId(),
                group.getName(),
                group.getDescription(),
                group.getCreatedBy(),
                group.getCreatedAt()
        );
    }

    // Convert GroupMember entity to GroupMemberResponse DTO
    private GroupMemberResponse toMemberResponse(GroupMember member) {
        return new GroupMemberResponse(
                member.getId(),
                member.getGroup().getId(),
                member.getGroup().getName(),
                member.getUserEmail(),
                member.getRole(),
                member.getJoinedAt()
        );
    }

    // Create a new group
    public GroupResponse createGroup(GroupRequest request, String userEmail) {
        Group group = new Group();
        group.setName(request.getName());
        group.setDescription(request.getDescription());
        group.setCreatedBy(userEmail);
        Group saved = groupRepository.save(group);

        // Creator automatically becomes ADMIN member
        GroupMember member = new GroupMember();
        member.setGroup(saved);
        member.setUserEmail(userEmail);
        member.setRole("ADMIN");
        groupMemberRepository.save(member);

        return toGroupResponse(saved);
    }

    // Get all groups
    public List<GroupResponse> getAllGroups() {
        return groupRepository.findAll()
                .stream()
                .map(this::toGroupResponse)
                .collect(Collectors.toList());
    }

    // Get groups created by user
    public List<GroupResponse> getMyGroups(String userEmail) {
        return groupRepository.findByCreatedBy(userEmail)
                .stream()
                .map(this::toGroupResponse)
                .collect(Collectors.toList());
    }

    // Join a group
    public GroupMemberResponse joinGroup(Long groupId, String userEmail) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));

        // Check if already a member
        groupMemberRepository.findByGroupIdAndUserEmail(groupId, userEmail)
                .ifPresent(m -> {
                    throw new RuntimeException("Already a member of this group");
                });

        GroupMember member = new GroupMember();
        member.setGroup(group);
        member.setUserEmail(userEmail);
        member.setRole("MEMBER");

        return toMemberResponse(groupMemberRepository.save(member));
    }

    // Get all members of a group
    public List<GroupMemberResponse> getGroupMembers(Long groupId) {
        groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));

        return groupMemberRepository.findByGroupId(groupId)
                .stream()
                .map(this::toMemberResponse)
                .collect(Collectors.toList());
    }

    // Get all groups a user has joined
    public List<GroupMemberResponse> getJoinedGroups(String userEmail) {
        return groupMemberRepository.findByUserEmail(userEmail)
                .stream()
                .map(this::toMemberResponse)
                .collect(Collectors.toList());
    }

    // Leave a group
    public void leaveGroup(Long groupId, String userEmail) {
        GroupMember member = groupMemberRepository
                .findByGroupIdAndUserEmail(groupId, userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("You are not a member of this group"));

        groupMemberRepository.delete(member);
    }
    // Check if user is ADMIN of a group
private void checkIfAdmin(Long groupId, String userEmail) {
    GroupMember member = groupMemberRepository
            .findByGroupIdAndUserEmail(groupId, userEmail)
            .orElseThrow(() -> new RuntimeException("You are not a member of this group"));

    if (!member.getRole().equals("ADMIN")) {
        throw new RuntimeException("You are not an admin of this group");
    }
}

// Delete a group (ADMIN only)
public void deleteGroup(Long groupId, String userEmail) {
    groupRepository.findById(groupId)
            .orElseThrow(() -> new ResourceNotFoundException("Group not found"));

    checkIfAdmin(groupId, userEmail);

    // Delete all members first
    groupMemberRepository.findByGroupId(groupId)
            .forEach(groupMemberRepository::delete);

    groupRepository.deleteById(groupId);
}

// Promote a member to ADMIN (ADMIN only)
public GroupMemberResponse promoteMember(Long groupId, String targetEmail, String userEmail) {
    groupRepository.findById(groupId)
            .orElseThrow(() -> new ResourceNotFoundException("Group not found"));

    checkIfAdmin(groupId, userEmail);

    GroupMember member = groupMemberRepository
            .findByGroupIdAndUserEmail(groupId, targetEmail)
            .orElseThrow(() -> new ResourceNotFoundException("Member not found in this group"));

    member.setRole("ADMIN");
    return toMemberResponse(groupMemberRepository.save(member));
}
}