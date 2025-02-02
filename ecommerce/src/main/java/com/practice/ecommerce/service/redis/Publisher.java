package com.practice.ecommerce.service.redis;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.connection.stream.StringRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class Publisher {

    private static final Logger LOGGER = LoggerFactory.getLogger(Publisher.class);
	private static final String STREAM = "notification";

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	public String publishToStream(Map<String, String> message) {
		try {
			StringRecord record = StreamRecords.string(message).withStreamKey(STREAM);
			redisTemplate.opsForStream().add(record);
			LOGGER.info("Data sent to Redis STREAM" );
			return "SUCCESS";
		} catch (Exception ex) {
			LOGGER.error("Failed to publish STREAM - " + ex.getMessage());
			ex.printStackTrace();
		}
		return null;
	}
}
