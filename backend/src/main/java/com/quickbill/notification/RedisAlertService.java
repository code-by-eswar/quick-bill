package com.quickbill.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisAlertService {

    private static final String LOW_STOCK_ALERT_KEY =
            "quickbill:alerts:low-stock:";

    private final RedisTemplate<String, String> redisTemplate;

    public void markAlertAsSent(Long productId) {

        String key =
                LOW_STOCK_ALERT_KEY + productId;

        redisTemplate
                .opsForValue()
                .set(key, "SENT");
    }

    public boolean isAlertAlreadySent(Long productId) {

        String key =
                LOW_STOCK_ALERT_KEY + productId;

        return Boolean.TRUE.equals(
                redisTemplate.hasKey(key));
    }

    public void removeAlert(Long productId) {

    String key =
            LOW_STOCK_ALERT_KEY + productId;

    redisTemplate.delete(key);
}
}