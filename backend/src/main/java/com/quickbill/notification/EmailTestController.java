package com.quickbill.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmailTestController {

    private final LowStockEmailService lowStockEmailService;

    @GetMapping("/api/test-email")
    public String sendTestEmail() {

        lowStockEmailService.sendEmail(
                "eswarchinnam70356@gmail.com",
                "QuickBill Test Email",
                "Congratulations! Email integration is working.");

        return "Email sent successfully";
    }
}