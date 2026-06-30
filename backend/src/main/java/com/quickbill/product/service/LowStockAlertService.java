package com.quickbill.product.service;

import com.quickbill.exception.ResourceNotFoundException;
import com.quickbill.notification.LowStockEmailService;
import com.quickbill.notification.RedisAlertService;
import com.quickbill.product.entity.Product;
import com.quickbill.product.repository.ProductRepository;
import com.quickbill.user.entity.Role;
import com.quickbill.user.entity.User;
import com.quickbill.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LowStockAlertService {

    private static final Logger log =
            LoggerFactory.getLogger(LowStockAlertService.class);

    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    private final RedisAlertService redisAlertService;

    private final LowStockEmailService lowStockEmailService;

    public void checkLowStockProducts() {

        log.info("Starting low stock products check");

        // Step 1 : Fetch all low stock products
        List<Product> lowStockProducts =
                productRepository.findLowStockProducts();

        if (lowStockProducts.isEmpty()) {

            log.info("No low stock products found");

            return;
        }

        // Step 2 : Filter only newly detected low stock products
        List<Product> productsToNotify =
                new ArrayList<>();

        for (Product product : lowStockProducts) {

            boolean alreadyAlerted =
                    redisAlertService.isAlertAlreadySent(
                            product.getId());

            if (alreadyAlerted) {

                log.debug(
                        "Skipping product '{}' because alert was already sent",
                        product.getName());

                continue;
            }

            log.info(
                    "New low stock product detected: {}",
                    product.getName());

            productsToNotify.add(product);
        }

        // Step 3 : Nothing new to notify
        if (productsToNotify.isEmpty()) {

            log.info(
                    "No new low stock alerts to send");

            return;
        }

        // Step 4 : Find Admin
        User admin =
                userRepository.findByRole(Role.ADMIN)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Admin user not found"));

        // Step 5 : Build Email
        StringBuilder emailBody =
                new StringBuilder();

        emailBody.append("Hello Admin,\n\n")
                .append("The following products are newly detected as low stock:\n\n");

        for (Product product : productsToNotify) {

            emailBody.append("Product : ")
                    .append(product.getName())
                    .append("\nCurrent Stock : ")
                    .append(product.getStockQuantity())
                    .append("\nMinimum Stock : ")
                    .append(product.getMinimumStockLevel())
                    .append("\n\n");
        }

        // Step 6 : Send Email
        lowStockEmailService.sendEmail(
                admin.getEmail(),
                "QuickBill Low Stock Alert",
                emailBody.toString());

        log.info(
                "Low stock email sent successfully to {}",
                admin.getEmail());

        // Step 7 : Mark products as alerted in Redis
        for (Product product : productsToNotify) {

            redisAlertService.markAlertAsSent(
                    product.getId());

            log.debug(
                    "Marked product '{}' as alerted in Redis",
                    product.getName());
        }

        log.info(
                "Total newly alerted products: {}",
                productsToNotify.size());
    }
}