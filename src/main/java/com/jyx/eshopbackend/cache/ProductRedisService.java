package com.jyx.eshopbackend.cache;

import com.jyx.eshopbackend.dto.ProductDetailDTO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

@Service
public class ProductRedisService {

    private static final Logger logger = LoggerFactory.getLogger(ProductRedisService.class);

    private final RedisTemplate<String, Object> redisTemplate;

    public ProductRedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 保存商品详情到 Redis
     */
    public void saveProductDetails(String productId, ProductDetailDTO productDetails) {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        ops.set(productId, productDetails,30, TimeUnit.MINUTES);
        logger.info("Product details saved to Redis for productId: {}", productId);
    }

    /**
     * 从 Redis 获取商品详情
     */
    public ProductDetailDTO getProductDetails(String productId) {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        Object product = ops.get(productId);
        if (product instanceof ProductDetailDTO) {
            return (ProductDetailDTO) product;
        }
        logger.warn("Product details not found in Redis for productId: {}", productId);
        return null;
    }
}
