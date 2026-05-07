package com.quickbill.user.service;

import com.quickbill.user.dto.UserRequest;
import com.quickbill.user.dto.UserResponse;
import com.quickbill.user.entity.User;
import com.quickbill.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.quickbill.exception.ResourceAlreadyExistsException;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse createUser(UserRequest request) {

        log.info("Attempting to create user with email: {}", request.getEmail());

        // check duplicate email
        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Email already exists: {}", request.getEmail());
            throw new ResourceAlreadyExistsException("Email already exists");
        }

        // check duplicate phone
        if (userRepository.existsByPhone(request.getPhone())) {
            log.warn("Phone already exists: {}", request.getPhone());
            throw new ResourceAlreadyExistsException("Phone already exists");
        }

        // map request → entity
        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .isActive(true)
                .build();

        User savedUser = userRepository.save(user);

        log.info("User created successfully with id: {}", savedUser.getId());

        User dbUser = userRepository.findById(savedUser.getId())
                .orElseThrow(() -> new RuntimeException("User not found after save"));

        return mapToResponse(dbUser);
    }

    // 🔥 mapper method
    private UserResponse mapToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole().name())
                .isActive(user.getIsActive())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}