package com.quickbill.product.controller;

import com.quickbill.product.dto.ProductRequest;
import com.quickbill.product.dto.ProductResponse;
import com.quickbill.product.service.ProductService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private static final Logger log =
            LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestBody ProductRequest request) {

        log.info("Received request to create product");

        ProductResponse response =
                productService.createProduct(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
    @GetMapping
public ResponseEntity<List<ProductResponse>>
getAllProducts() {

    return ResponseEntity.ok(
            productService.getAllProducts()
    );
}
@GetMapping("/{id}")
public ResponseEntity<ProductResponse>
getProductById(
        @PathVariable Long id) {

    return ResponseEntity.ok(
            productService.getProductById(id));
}
@PutMapping("/{id}")
public ResponseEntity<ProductResponse>
updateProduct(
        @PathVariable Long id,
        @Valid @RequestBody ProductRequest request) {

    return ResponseEntity.ok(
            productService.updateProduct(
                    id,
                    request));
}
@DeleteMapping("/{id}")
public ResponseEntity<Void>
deactivateProduct(
        @PathVariable Long id) {

    productService.deactivateProduct(id);

    return ResponseEntity.noContent()
            .build();
}
@GetMapping("/search")
public ResponseEntity<List<ProductResponse>>
searchProducts(
        @RequestParam String name) {

    return ResponseEntity.ok(
            productService.searchProducts(name));
}
@GetMapping("/barcode/{barcode}")
public ResponseEntity<ProductResponse>
getProductByBarcode(
        @PathVariable String barcode) {

    return ResponseEntity.ok(
            productService.getProductByBarcode(
                    barcode));
}
@GetMapping("/low-stock")
public ResponseEntity<List<ProductResponse>>
getLowStockProducts() {

    return ResponseEntity.ok(
            productService.getLowStockProducts()
    );
}
}