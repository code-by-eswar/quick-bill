package com.quickbill.cache;

import com.quickbill.product.dto.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductCacheService {

    private static final String PRODUCT_CACHE_KEY =
            "quickbill:product:barcode:";

    private final RedisTemplate<String, ProductResponse>
            productRedisTemplate;

    public ProductResponse getProductFromCache(
        String barcode) {

    String key =
            PRODUCT_CACHE_KEY + barcode;

    return productRedisTemplate
            .opsForValue()
            .get(key);

}
public void cacheProduct(
        String barcode,
        ProductResponse product) {

    String key =
            PRODUCT_CACHE_KEY + barcode;

    productRedisTemplate
            .opsForValue()
            .set(key, product);
}
}