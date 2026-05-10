package com.quickbill.auth.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {

    private Long userId;

    private String fullName;

    private String email;

    private String role;

    private String message;
    private String token;
}