package com.quickbill.product.service;

import com.quickbill.notification.LowStockEmailService;
import com.quickbill.product.entity.Product;
import com.quickbill.product.repository.ProductRepository;
import com.quickbill.user.entity.Role;
import com.quickbill.user.entity.User;
import com.quickbill.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LowStockAlertService {

    private static final Logger log =
            LoggerFactory.getLogger(
                    LowStockAlertService.class);

    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    private final LowStockEmailService
            lowStockEmailService;

    public void checkLowStockProducts() {

        log.info(
                "Starting low stock products check");

        List<Product> lowStockProducts =
                productRepository.findLowStockProducts();

        if (lowStockProducts.isEmpty()) {

            log.info(
                    "No low stock products found");

            return;
        }

        User admin =
                userRepository.findByRole(Role.ADMIN)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Admin user not found"));

        String adminEmail =
                admin.getEmail();

        StringBuilder emailBody =
                new StringBuilder();

        emailBody.append(
                "Hello Admin,\n\n")
                .append(
                        "The following products are low in stock:\n\n");

        for (Product product : lowStockProducts) {

            emailBody.append("Product: ")
                    .append(product.getName())
                    .append("\nCurrent Stock: ")
                    .append(product.getStockQuantity())
                    .append("\nMinimum Stock: ")
                    .append(product.getMinimumStockLevel())
                    .append("\n\n");
        }

        lowStockEmailService.sendEmail(
                adminEmail,
                "QuickBill Low Stock Alert",
                emailBody.toString());

        log.info(
                "Low stock alert email sent successfully to {}",
                adminEmail);
    }
}