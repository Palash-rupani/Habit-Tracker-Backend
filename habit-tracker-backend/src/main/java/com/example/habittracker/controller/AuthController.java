package com.example.habittracker.controller;
import com.example.habittracker.security.JwtUtil;

import com.example.habittracker.entity.User;
import com.example.habittracker.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.register(user);
    }
    @PostMapping("/login")
public String login(@RequestBody User user) {

    User loggedUser =
            userService.login(user.getEmail(), user.getPassword());

    return JwtUtil.generateToken(loggedUser.getEmail());
}
    
}
