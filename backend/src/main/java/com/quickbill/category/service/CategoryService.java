package com.quickbill.category.service;

import com.quickbill.category.dto.CategoryRequest;
import com.quickbill.category.dto.CategoryResponse;
import com.quickbill.category.entity.Category;
import com.quickbill.category.repository.CategoryRepository;
import com.quickbill.exception.ResourceAlreadyExistsException;
import com.quickbill.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private static final Logger log =
            LoggerFactory.getLogger(CategoryService.class);

    private final CategoryRepository categoryRepository;

    public CategoryResponse createCategory(
            CategoryRequest request) {

        log.info("Creating category with name: {}",
                request.getName());

        if (categoryRepository.existsByName(
                request.getName())) {

            log.warn("Category already exists: {}",
                    request.getName());

            throw new ResourceAlreadyExistsException(
                    "Category already exists");
        }

        Category category = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .isActive(true)
                .build();

        Category savedCategory =
                categoryRepository.save(category);

        log.info("Category created successfully with id: {}",
                savedCategory.getId());

        return mapToResponse(savedCategory);
    }

    public List<CategoryResponse> getAllCategories() {

        log.info("Fetching all active categories");

        return categoryRepository.findByIsActiveTrue()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public CategoryResponse getCategoryById(
            Long id) {

        log.info("Fetching category with id: {}",
                id);

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> {

                    log.error("Category not found with id: {}",
                            id);

                    return new ResourceNotFoundException(
                            "Category not found");
                });

        return mapToResponse(category);
    }

    public CategoryResponse updateCategory(
            Long id,
            CategoryRequest request) {

        log.info("Updating category with id: {}",
                id);

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> {

                    log.error("Category not found with id: {}",
                            id);

                    return new ResourceNotFoundException(
                            "Category not found");
                });

        category.setName(request.getName());
        category.setDescription(request.getDescription());

        Category updatedCategory =
                categoryRepository.save(category);

        log.info("Category updated successfully: {}",
                updatedCategory.getId());

        return mapToResponse(updatedCategory);
    }

    public void deactivateCategory(
            Long id) {

        log.info("Deactivating category with id: {}",
                id);

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> {

                    log.error("Category not found with id: {}",
                            id);

                    return new ResourceNotFoundException(
                            "Category not found");
                });

        category.setIsActive(false);

        categoryRepository.save(category);

        log.info("Category deactivated successfully: {}",
                id);
    }

    private CategoryResponse mapToResponse(
            Category category) {

        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .isActive(category.getIsActive())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }
}