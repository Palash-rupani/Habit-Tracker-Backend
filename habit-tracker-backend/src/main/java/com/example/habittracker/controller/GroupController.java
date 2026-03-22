package com.example.habittracker.controller;

import com.example.habittracker.dto.GroupMemberResponse;
import com.example.habittracker.dto.GroupRequest;
import com.example.habittracker.dto.GroupResponse;
import com.example.habittracker.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
@CrossOrigin(origins = "*")
public class GroupController {

    @Autowired
    private GroupService groupService;

    private String getLoggedInEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    // CREATE a group
    @PostMapping
    public ResponseEntity<GroupResponse> createGroup(@RequestBody GroupRequest request) {
        String email = getLoggedInEmail();
        GroupResponse response = groupService.createGroup(request, email);
        return ResponseEntity.ok(response);
    }

    // GET all groups (browse available groups)
    @GetMapping
    public ResponseEntity<List<GroupResponse>> getAllGroups() {
        return ResponseEntity.ok(groupService.getAllGroups());
    }

    // GET groups created by logged-in user
    @GetMapping("/my")
    public ResponseEntity<List<GroupResponse>> getMyGroups() {
        String email = getLoggedInEmail();
        return ResponseEntity.ok(groupService.getMyGroups(email));
    }

    // GET all groups joined by logged-in user
    @GetMapping("/joined")
    public ResponseEntity<List<GroupMemberResponse>> getJoinedGroups() {
        String email = getLoggedInEmail();
        return ResponseEntity.ok(groupService.getJoinedGroups(email));
    }

    // JOIN a group
    @PostMapping("/{id}/join")
    public ResponseEntity<GroupMemberResponse> joinGroup(@PathVariable Long id) {
        String email = getLoggedInEmail();
        GroupMemberResponse response = groupService.joinGroup(id, email);
        return ResponseEntity.ok(response);
    }

    // GET all members of a group
    @GetMapping("/{id}/members")
    public ResponseEntity<List<GroupMemberResponse>> getGroupMembers(@PathVariable Long id) {
        return ResponseEntity.ok(groupService.getGroupMembers(id));
    }

    // LEAVE a group
    @DeleteMapping("/{id}/leave")
    public ResponseEntity<String> leaveGroup(@PathVariable Long id) {
        String email = getLoggedInEmail();
        groupService.leaveGroup(id, email);
        return ResponseEntity.ok("Left group successfully");
    }
    // DELETE a group (ADMIN only)
@DeleteMapping("/{id}")
public ResponseEntity<String> deleteGroup(@PathVariable Long id) {
    String email = getLoggedInEmail();
    groupService.deleteGroup(id, email);
    return ResponseEntity.ok("Group deleted successfully");
}

// PROMOTE a member to ADMIN (ADMIN only)
@PutMapping("/{id}/promote/{memberEmail}")
public ResponseEntity<GroupMemberResponse> promoteMember(
        @PathVariable Long id,
        @PathVariable String memberEmail) {
    String email = getLoggedInEmail();
    GroupMemberResponse response = groupService.promoteMember(id, memberEmail, email);
    return ResponseEntity.ok(response);
}
}