package com.quickbill.user.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class UserResponse {

    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String role;
    private Boolean isActive;
    private Instant createdAt;
    private Instant updatedAt;
}