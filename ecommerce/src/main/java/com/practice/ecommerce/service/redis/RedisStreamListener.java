package com.practice.ecommerce.service.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Service;

@Service
public class RedisStreamListener implements StreamListener<String, MapRecord<String, String, String>> {

	private static final Logger logger = LoggerFactory.getLogger(RedisStreamListener.class);

    @Override
	public void onMessage(MapRecord<String, String, String> message) {
		logger.info("Received STREAM: " + message.getStream() + " ID: " + message.getId() +
				" Message: " + message.getValue()
		);
	}
}
