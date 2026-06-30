package com.quickbill.product.service;

import com.quickbill.category.entity.Category;
import com.quickbill.category.repository.CategoryRepository;
import com.quickbill.product.dto.ProductRequest;
import com.quickbill.product.dto.ProductResponse;
import com.quickbill.product.entity.Product;
import com.quickbill.product.repository.ProductRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import com.quickbill.exception.ResourceAlreadyExistsException;
import com.quickbill.exception.ResourceNotFoundException;
import com.quickbill.notification.RedisAlertService;

@Service
@RequiredArgsConstructor
public class ProductService {

    private static final Logger log =
            LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;
    private final RedisAlertService redisAlertService;
    public ProductResponse createProduct(
        ProductRequest request) {

    log.info("Creating product: {}",
            request.getName());

    Category category = categoryRepository.findById(
                    request.getCategoryId())
            .orElseThrow(() -> {

                log.error(
                        "Category not found: {}",
                        request.getCategoryId());

                return new RuntimeException(
                        "Category not found");
            });

    if (request.getBarcode() != null
            && !request.getBarcode().isBlank()
            && productRepository.findByBarcode(
                    request.getBarcode()).isPresent()) {

        throw new RuntimeException(
                "Barcode already exists");
    }

    Product product = Product.builder()
            .name(request.getName())
            .barcode(request.getBarcode())
            .purchasePrice(
                    request.getPurchasePrice())
            .sellingPrice(
                    request.getSellingPrice())
            .stockQuantity(
                    request.getStockQuantity())
            .minimumStockLevel(
                    request.getMinimumStockLevel())
            .category(category)
            .build();

    Product savedProduct =
            productRepository.save(product);

    log.info("Product created successfully: {}",
            savedProduct.getId());

    return mapToResponse(savedProduct);
}
private ProductResponse mapToResponse(
        Product product) {

    return ProductResponse.builder()
            .id(product.getId())
            .name(product.getName())
            .barcode(product.getBarcode())
            .purchasePrice(
                    product.getPurchasePrice())
            .sellingPrice(
                    product.getSellingPrice())
            .stockQuantity(
                    product.getStockQuantity())
            .minimumStockLevel(
                    product.getMinimumStockLevel())
            .isActive(product.getIsActive())
            .categoryId(
                    product.getCategory().getId())
            .categoryName(
                    product.getCategory().getName())
            .createdAt(product.getCreatedAt())
            .updatedAt(product.getUpdatedAt())
            .build();
}
public List<ProductResponse> getAllProducts() {
    log.info("Fetching all active products");
    return productRepository.findByIsActiveTrue()
            .stream()
            .map(this::mapToResponse)
            .toList();
}
public ProductResponse getProductById(
        Long id) {

    log.info("Fetching product with id: {}",
            id);

    Product product =
            productRepository.findById(id)
                    .orElseThrow(() -> {

                        log.error(
                                "Product not found with id: {}",
                                id);

                        return new ResourceNotFoundException(
                                "Product not found");
                    });

    return mapToResponse(product);
}
public ProductResponse updateProduct(
        Long id,
        ProductRequest request) {

    log.info("Updating product with id: {}",
            id);

    Product product =
            productRepository.findById(id)
                    .orElseThrow(() -> {

                        log.error(
                                "Product not found with id: {}",
                                id);

                        return new ResourceNotFoundException(
                                "Product not found");
                    });

    Category category =
            categoryRepository.findById(
                    request.getCategoryId())
                    .orElseThrow(() -> {

                        log.error(
                                "Category not found with id: {}",
                                request.getCategoryId());

                        return new ResourceNotFoundException(
                                "Category not found");
                    });

    if (request.getBarcode() != null
            && !request.getBarcode().isBlank()) {

        productRepository.findByBarcode(
                        request.getBarcode())
                .ifPresent(existingProduct -> {

                    if (!existingProduct.getId()
                            .equals(id)) {

                        throw new ResourceAlreadyExistsException(
                                "Barcode already exists");
                    }
                });
    }

    product.setName(request.getName());
    product.setBarcode(request.getBarcode());
    product.setPurchasePrice(
            request.getPurchasePrice());
    product.setSellingPrice(
            request.getSellingPrice());
    product.setStockQuantity(
            request.getStockQuantity());
    product.setMinimumStockLevel(
            request.getMinimumStockLevel());
    product.setCategory(category);
Product updatedProduct =
        productRepository.save(product);

if (updatedProduct.getStockQuantity()
        > updatedProduct.getMinimumStockLevel()) {

    redisAlertService.removeAlert(
            updatedProduct.getId());
}

log.info("Product updated successfully: {}",
        updatedProduct.getId());

return mapToResponse(updatedProduct);
   
}
public void deactivateProduct(
        Long id) {

    log.info("Deactivating product with id: {}",
            id);

    Product product =
            productRepository.findById(id)
                    .orElseThrow(() -> {

                        log.error(
                                "Product not found with id: {}",
                                id);

                        return new ResourceNotFoundException(
                                "Product not found");
                    });

    product.setIsActive(false);

    productRepository.save(product);

    log.info("Product deactivated successfully: {}",
            id);
}
public List<ProductResponse> searchProducts(
        String name) {

    log.info("Searching products with name: {}",
            name);

    return productRepository
            .findByNameContainingIgnoreCase(name)
            .stream()
            .filter(Product::getIsActive)
            .map(this::mapToResponse)
            .toList();
}
public ProductResponse getProductByBarcode(
        String barcode) {

    log.info("Fetching product by barcode: {}",
            barcode);

    Product product =
            productRepository.findByBarcode(barcode)
                    .orElseThrow(() -> {

                        log.error(
                                "Product not found with barcode: {}",
                                barcode);

                        return new ResourceNotFoundException(
                                "Product not found");
                    });

    if (!product.getIsActive()) {

        throw new ResourceNotFoundException(
                "Product not found");
    }

    return mapToResponse(product);
}
public List<ProductResponse> getLowStockProducts() {

    log.info("Fetching low stock products");

    return productRepository.findLowStockProducts()
            .stream()
            .map(this::mapToResponse)
            .toList();
}
}