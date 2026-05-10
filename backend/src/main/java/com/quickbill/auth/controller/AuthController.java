package com.quickbill.auth.controller;

import com.quickbill.auth.dto.LoginRequest;
import com.quickbill.auth.dto.LoginResponse;
import com.quickbill.auth.service.AuthService;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger log =
            LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        log.info("Received login request for email: {}",
                request.getEmail());

        LoginResponse response = authService.login(request);

        log.info("Login API completed successfully for email: {}",
                request.getEmail());

        return ResponseEntity.ok(response);
    }
}