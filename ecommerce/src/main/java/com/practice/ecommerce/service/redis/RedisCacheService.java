package com.practice.ecommerce.service.redis;

import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.core.JsonProcessingException;
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
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(o.toString(), entityClass);
        } catch (JsonProcessingException ex) {
            logger.info("Failed to parse Object: " + ex.getMessage());
        } catch (NullPointerException ex) {
            logger.info("Object is NULL: " + ex.getMessage());
        }

        return null;
    }

    public void setCache(String key, Object o, Integer ttl) {
        try {
            String jsonValue = new ObjectMapper().writeValueAsString(o);
            redisTemplate.opsForValue().set(key, jsonValue, ttl, TimeUnit.MINUTES);
        } catch (Exception ex) {
            logger.info("Failed to save in cache: " + ex.getMessage());
        }
    }

    public void deleteCache(String key) {
        try {
            redisTemplate.delete(key);
        } catch (Exception ex) {
            logger.info("Failed to save in cache: " + ex.getMessage());
        }
    }

    public void writeThroughCache(String key) {

    }
}
