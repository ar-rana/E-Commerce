package com.practice.ecommerce.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.practice.ecommerce.defaultModels.DefaultModels;
import com.practice.ecommerce.model.Product;
import com.practice.ecommerce.service.redis.Publisher;
import com.practice.ecommerce.service.redis.RedisCacheService;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.annotation.Rollback;

import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

        // relies on JavaBean property getters like getXyz() methods
        MatcherAssert.assertThat(fetchedProduct, samePropertyValuesAs(product)); // Hamcrest reflection to check objects are equal
        // does not require getters, work by using reflection
        assertTrue(new ReflectionEquals(product).matches(fetchedProduct)); // Mockito
    }

    @Test
    public void testObjectDeletion() {
        redisCacheService.setCache("deleteTest", product, 1);
        redisCacheService.deleteCache("deleteTest");

        Product fetchedProduct = redisCacheService.getCache("deleteTest", Product.class);

        assertNull(fetchedProduct);
    }

    @Test
    @Rollback
    public void testPublisherAndListener() {
        Map<String, String> map = new HashMap<>();
        map.put("key", uniqueKey);
        String attempt = publisher.publishToStream(map);

        assertNotNull(attempt);
        assertEquals("SUCCESS", attempt);
    }

    @Test
    @Rollback
    public void testGetMatchers() {
        redisCacheService.setCache("test/one", uniqueKey, 2);
        redisCacheService.setCache("test/two", uniqueKey, 2);
        redisCacheService.setCache("test/three", uniqueKey, 2);

        List<String> items = redisCacheService.getMatchers("test/*", String.class, 10);

        assertNull(items);
    }

    @Test
    @Rollback
    public void testGetMatchersAll() {
        redisCacheService.setCache("test/one", uniqueKey, 2);
        redisCacheService.setCache("test/two", uniqueKey, 2);
        redisCacheService.setCache("test/three", uniqueKey, 2);

        List<String> items = redisCacheService.getMatchers("test/*", String.class, 3);

        assertNotNull(items);
        assertEquals(3, items.size());
    }
}
