package com.practice.ecommerce.service.redis;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.ecommerce.model.Enums.EmailMessages;
import com.practice.ecommerce.model.Order;
import com.practice.ecommerce.model.Product;
import com.practice.ecommerce.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Service;

@Service
public class RedisStreamListener implements StreamListener<String, MapRecord<String, String, String>> {

	@Autowired
	private EmailService emailService;

	@Autowired
	private RedisTemplate redisTemplate;

	private static final String STREAM = "notification";
	private static final Logger logger = LoggerFactory.getLogger(RedisStreamListener.class);

    @Override
	public void onMessage(MapRecord<String, String, String> message) {
		logger.info("Received STREAM: " + message.getStream() + " ID: " + message.getId());

		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> map = message.getValue();
		if (map.get("type") == null || map.get("to") == null) {
			redisTemplate.opsForStream().delete(STREAM, message.getId());
		}

		EmailMessages type = EmailMessages.valueOf(map.get("type"));
		String to = map.get("to");
		System.out.println("map: " + map);

		switch (type) {
			case EmailMessages.welcomeMessage:
				emailService.sendWelcomeMail(to, type);
				redisTemplate.opsForStream().delete(STREAM, message.getId());
				break;
            case EmailMessages.orderPlaced:
                try {
                    emailService.sendMailWithAttachment(to, type, mapper.readValue(map.get("orders"), new TypeReference<List<Order>>() {}), map.get("referenceId"), map.get("paymentId"));
					redisTemplate.opsForStream().delete(STREAM, message.getId());
                } catch (JsonProcessingException ex) {
                    logger.error("ERROR Parsing Product in STREAM: {}, for: {}", ex.getMessage(), EmailMessages.orderPlaced);
                }
                break;
            case EmailMessages.productStockOver:
				emailService.sendHtmlMail(to, type, map.get("productId"));
				redisTemplate.opsForStream().delete(STREAM, message.getId());
				break;
			case EmailMessages.simpleMessage:
				emailService.sendSimpleMail(to, map.get("subject"), map.get("content"));
				redisTemplate.opsForStream().delete(STREAM, message.getId());
				break;
            default:
				redisTemplate.opsForStream().delete(STREAM, message.getId());
		}
	}
}
