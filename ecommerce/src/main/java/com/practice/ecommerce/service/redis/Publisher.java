package com.practice.ecommerce.service.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
public class Publisher {

    private static final Logger LOGGER = LoggerFactory.getLogger(Publisher.class);

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Autowired
	private ChannelTopic topic;

	public String publishToQueue(String input) {
		try {
			redisTemplate.convertAndSend(topic.getTopic(), input);
			LOGGER.info("Data - " + input + " sent through Redis Topic - " + topic.getTopic());
			return "SUCCESS";
		} catch (Exception ex) {
			LOGGER.info("Failed to publish - " + input + topic.getTopic());
			LOGGER.info("Failed to publish because - " + ex.getMessage());
		}
		return null;
	}
}
