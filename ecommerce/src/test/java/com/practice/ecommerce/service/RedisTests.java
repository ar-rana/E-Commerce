package com.practice.ecommerce.service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.practice.ecommerce.defaultModels.DefaultModels;
import com.practice.ecommerce.model.Product;
import com.practice.ecommerce.service.redis.Publisher;
import com.practice.ecommerce.service.redis.RedisCacheService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
public class RedisTests {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private RedisCacheService redisCacheService;

    @Autowired
    private Publisher publisher;

    private final String uniqueKey = UUID.randomUUID().toString();
    private Product product = DefaultModels.product;

    @Test
    public void testRedisCache() {
        redisTemplate.opsForValue().set("test", uniqueKey, 3, TimeUnit.MINUTES);
        String cacheValue = redisTemplate.opsForValue().get("test");

        assertEquals(uniqueKey, cacheValue);
    }

    @Test
    public void testObjectParsing() {
        redisCacheService.setCache("parsingTest", product, 1);

        Product fetchedProduct = redisCacheService.getCache("parsingTest", Product.class);

        assertEquals(product.toString(), fetchedProduct.toString());
    }

    @Test
    public void testObjectDeletion() {
        redisCacheService.deleteCache("parsingTest");

        Product fetchedProduct = redisCacheService.getCache("parsingTest", Product.class);

        assertNull(fetchedProduct);
    }

    @Test
    @Rollback
    public void testPublisherAndListener() {
        String attempt = publisher.publishToStream(uniqueKey);

        assertNotNull(attempt);
        assertEquals("SUCCESS", attempt);
    }
}
