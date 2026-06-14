package com.quickbill.product.dto;

import jakarta.validation.constraints.*;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequest {

    @NotBlank(message = "Product name is required")
    @Size(max = 150,
            message = "Product name cannot exceed 150 characters")
    private String name;

    private String barcode;

    @NotNull(message = "Purchase price is required")
    @DecimalMin(value = "0.0",
            inclusive = false,
            message = "Purchase price must be greater than zero")
    private BigDecimal purchasePrice;

    @NotNull(message = "Selling price is required")
    @DecimalMin(value = "0.0",
            inclusive = false,
            message = "Selling price must be greater than zero")
    private BigDecimal sellingPrice;

    @NotNull(message = "Stock quantity is required")
    @Min(value = 0,
            message = "Stock quantity cannot be negative")
    private Integer stockQuantity;

    @NotNull(message = "Minimum stock level is required")
    @Min(value = 0,
            message = "Minimum stock level cannot be negative")
    private Integer minimumStockLevel;

    @NotNull(message = "Category id is required")
    private Long categoryId;
}