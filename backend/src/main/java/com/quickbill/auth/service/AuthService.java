package com.quickbill.auth.service;

import com.quickbill.auth.dto.LoginRequest;
import com.quickbill.auth.dto.LoginResponse;
import com.quickbill.user.entity.User;
import com.quickbill.user.repository.UserRepository;
import com.quickbill.security.jwt.JwtService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AuthService {


  private static final Logger log =
        LoggerFactory.getLogger(AuthService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest request) {

    log.info("Login attempt for email: {}", request.getEmail());

    // fetch user by email
    User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> {

                log.warn("Login failed - user not found: {}", request.getEmail());

                return new RuntimeException("Invalid email or password");
            });

    // compare passwords
    boolean passwordMatches = passwordEncoder.matches(
            request.getPassword(),
            user.getPasswordHash()
    );

    // invalid password
    if (!passwordMatches) {

        log.warn("Login failed - invalid password for email: {}",
                request.getEmail());

        throw new RuntimeException("Invalid email or password");
    }

  // successful login
log.info("Login successful for user id: {}", user.getId());

// generate jwt token
String token = jwtService.generateToken(
        user.getEmail(),
        user.getRole().name()
);

log.info("JWT token generated for user: {}", user.getEmail());

return LoginResponse.builder()
        .userId(user.getId())
        .fullName(user.getFullName())
        .email(user.getEmail())
        .role(user.getRole().name())
        .token(token)
        .message("Login successful")
        .build();
}}