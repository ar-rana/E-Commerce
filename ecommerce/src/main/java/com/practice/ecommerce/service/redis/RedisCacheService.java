package com.practice.ecommerce.service.redis;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.ecommerce.model.Product;
import com.practice.ecommerce.model.SavedProduct;
import com.practice.ecommerce.model.compositeId.ListId;
import com.practice.ecommerce.repository.SavedProductsRepo;
import com.practice.ecommerce.service.SavedProductsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

@Service
public class RedisCacheService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SavedProductsRepo savedProductsRepo;

    private static final Logger logger = LoggerFactory.getLogger(RedisCacheService.class);

    public <T> T getCache(String key, Class<T> entityClass) {
        Object o = redisTemplate.opsForValue().get(key);
        if (o == null) {
            logger.info("Object Null in Cache with key: {}", key);
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        logger.info("Object as String from Cache for: {}", entityClass);
        try {
            return mapper.readValue((String) o, entityClass);
        } catch (Exception ex) {
            logger.error("Failed to PARSE OBJECT: " + ex.getMessage());
        }
        return null;
    }
    public <T> T getCache(String key, TypeReference<T> typeReference) {
        Object o = redisTemplate.opsForValue().get(key);
        if (o == null) {
            logger.info("Object Null in Cache with key: {}", key);
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        logger.info("Object as String from Cache for: {}", typeReference);
        try {
            return mapper.readValue((String) o, typeReference);
        } catch (JsonProcessingException ex) {
            logger.error("Failed to PARSE OBJECT: " + ex.getMessage());
        }
        return null;
    }

    public <T> List<T> getMatchers(String match, Class<T> entityClass, Integer limit) {
        ScanOptions scanOptions = ScanOptions.scanOptions().match(match).count(limit).build();
        Set<String> keys = new HashSet<>();

        try (var cursor = redisTemplate.scan(scanOptions)) { // cursor is a ClosableIterator
            while (cursor.hasNext() && keys.size() < limit) {
                keys.add((String) cursor.next());
            }
        }

        if (keys.size() < limit) {
            return null;
        }
        List<T> items = new ArrayList<>();
        for (String key: keys) {
            items.add(getCache(key, entityClass));
        }

        return items;
    }

    public void setCache(String key, Object o, Integer ttl) {
        try {
            String jsonValue = new ObjectMapper().writeValueAsString(o);
            redisTemplate.opsForValue().set(key, jsonValue, ttl, TimeUnit.MINUTES);
        } catch (Exception ex) {
            logger.info("Failed to SAVE in cache: " + ex.getMessage());
        }
    }

    public void deleteCache(String key) {
        try {
            redisTemplate.delete(key);
        } catch (Exception ex) {
            logger.error("Failed to DELETE cache: " + ex.getMessage());
        }
        logger.warn("DELETED " + key + " from cache");
    }

    public void writeThroughCache(String key, Map<String, String> data) {
        ObjectMapper mapper = new ObjectMapper();
        Optional<SavedProduct> savedProduct = null;
        ListId listId = null;
        try {
            listId = mapper.readValue(data.get("listId"), ListId.class);
            savedProduct = savedProductsRepo.findById(listId);
        } catch (JsonProcessingException ex) {
            logger.error("FAILED TO ADD DATA TO SAVEDPRODUCT REPO: {}", ex.getMessage());
            deleteCache(key);
            return;
        }
        if (savedProduct.isPresent()) {
            List<Product> productList = getCache(key, new TypeReference<List<Product>>() {});
            savedProduct.get().setProducts(productList);
            savedProductsRepo.save(savedProduct.get());
        }
    }
}
