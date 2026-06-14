package com.quickbill.category.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class CategoryResponse {

    private Long id;

    private String name;

    private String description;

    private Boolean isActive;

    private Instant createdAt;

    private Instant updatedAt;
}