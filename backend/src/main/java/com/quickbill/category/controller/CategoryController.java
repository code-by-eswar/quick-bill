package com.quickbill.category.controller;

import com.quickbill.category.dto.CategoryRequest;
import com.quickbill.category.dto.CategoryResponse;
import com.quickbill.category.service.CategoryService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private static final Logger log =
            LoggerFactory.getLogger(CategoryController.class);

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(
            @Valid @RequestBody CategoryRequest request) {

        log.info("Received request to create category");

        CategoryResponse response =
                categoryService.createCategory(request);

        log.info("Category created successfully");

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
    @GetMapping
public ResponseEntity<List<CategoryResponse>>
getAllCategories() {

    log.info("Received request to fetch categories");

    return ResponseEntity.ok(
            categoryService.getAllCategories()
    );
}
@GetMapping("/{id}")
public ResponseEntity<CategoryResponse>
getCategoryById(@PathVariable Long id) {

    log.info("Received request for category id: {}", id);

    return ResponseEntity.ok(
            categoryService.getCategoryById(id)
    );
}
@PutMapping("/{id}")
public ResponseEntity<CategoryResponse>
updateCategory(
        @PathVariable Long id,
        @Valid @RequestBody CategoryRequest request) {

    log.info("Received request to update category: {}", id);

    return ResponseEntity.ok(
            categoryService.updateCategory(id, request)
    );
}
@DeleteMapping("/{id}")
public ResponseEntity<Void> deactivateCategory(
        @PathVariable Long id) {

    log.info("Received request to deactivate category: {}", id);

    categoryService.deactivateCategory(id);

    return ResponseEntity.noContent().build();
}
}