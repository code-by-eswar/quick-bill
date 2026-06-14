package com.quickbill.product.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
public class ProductResponse {

    private Long id;

    private String name;

    private String barcode;

    private BigDecimal purchasePrice;

    private BigDecimal sellingPrice;

    private Integer stockQuantity;

    private Integer minimumStockLevel;

    private Boolean isActive;

    private Long categoryId;

    private String categoryName;

    private Instant createdAt;

    private Instant updatedAt;
}