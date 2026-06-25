package com.quickbill.scheduler;

import com.quickbill.product.service.LowStockAlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LowStockScheduler {

    private final LowStockAlertService
            lowStockAlertService;

     @Scheduled(cron = "0 * * * * *")
    public void monitorLowStock() {
      System.out.println("SCHEDULER TRIGGERED");
        lowStockAlertService
                .checkLowStockProducts();
    }
}