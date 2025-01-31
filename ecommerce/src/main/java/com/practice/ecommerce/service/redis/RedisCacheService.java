package com.practice.ecommerce.service.redis;

import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisCacheService {

    @Autowired
    private RedisTemplate redisTemplate;

    private static final Logger logger = LoggerFactory.getLogger(RedisCacheService.class);

    public <T> T getCache(String key, Class<T> entityClass) {
        Object o = redisTemplate.opsForValue().get(key);
        if (o == null) {
            logger.info("Object Null in Cache with key: {}", key);
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        logger.info("Object as String from Cache: {}", o);
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
        logger.info("Object as String from Cache: {}", o);
        try {
            return mapper.readValue((String) o, typeReference);
        } catch (JsonProcessingException ex) {
            logger.error("Failed to PARSE OBJECT: " + ex.getMessage());
        }
        return null;
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

    public void writeThroughCache(String key) {

    }
}
