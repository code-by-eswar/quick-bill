package com.quickbill.product.repository;

import com.quickbill.product.entity.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface ProductRepository
        extends JpaRepository<Product, Long> {

    Optional<Product> findByBarcode(String barcode);

    List<Product> findByNameContainingIgnoreCase(String name);

    List<Product> findByIsActiveTrue();

    List<Product> findByStockQuantityLessThanEqual(
            Integer minimumStockLevel);
   @Query("""
       SELECT p
       FROM Product p
       WHERE p.stockQuantity <= p.minimumStockLevel
       AND p.isActive = true
       """)
List<Product> findLowStockProducts();
        }