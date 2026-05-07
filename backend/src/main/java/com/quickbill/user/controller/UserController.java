package com.quickbill.user.controller;

import com.quickbill.user.dto.UserRequest;
import com.quickbill.user.dto.UserResponse;
import com.quickbill.user.service.UserService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    // constructor injection
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // CREATE USER
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest request) {

        UserResponse createdUser = userService.createUser(request);

        return ResponseEntity.status(201).body(createdUser);
    }
}