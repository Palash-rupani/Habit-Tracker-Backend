package com.example.habittracker.dto;

import java.time.LocalDateTime;

public class GroupMemberResponse {
    private Long id;
    private Long groupId;
    private String groupName;
    private String userEmail;
    private String role;
    private LocalDateTime joinedAt;

    public GroupMemberResponse(Long id, Long groupId, String groupName,
                               String userEmail, String role,
                               LocalDateTime joinedAt) {
        this.id = id;
        this.groupId = groupId;
        this.groupName = groupName;
        this.userEmail = userEmail;
        this.role = role;
        this.joinedAt = joinedAt;
    }

    public Long getId() { return id; }
    public Long getGroupId() { return groupId; }
    public String getGroupName() { return groupName; }
    public String getUserEmail() { return userEmail; }
    public String getRole() { return role; }
    public LocalDateTime getJoinedAt() { return joinedAt; }
}