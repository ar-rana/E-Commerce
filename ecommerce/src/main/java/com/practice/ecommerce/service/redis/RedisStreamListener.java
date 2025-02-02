package com.practice.ecommerce.service.redis;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.ecommerce.model.Enums.EmailMessages;
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
		logger.info("Received STREAM: " + message.getStream() + " ID: " + message.getId() +
				" Message: " + message.getValue()
		);

		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> map = message.getValue();

		EmailMessages type = EmailMessages.valueOf(map.get("type"));
		String to = map.get("to");
		System.out.println("map: " + map);

		switch (type) {
			case EmailMessages.welcomeMessage:
				emailService.sendWelcomeMail(to, type);
				redisTemplate.opsForStream().delete(STREAM, message.getId());
				break;
            case orderPlaced:
                break;
            case EmailMessages.productStockOver:
                try {
                    emailService.sendHtmlMail(to, type, mapper.readValue(map.get("product"), Product.class));
					redisTemplate.opsForStream().delete(STREAM, message.getId());
                } catch (JsonProcessingException ex) {
                    logger.error("ERROR Parsing Product in STREAM: {}", ex.getMessage());
                }
            default:
				emailService.sendSimpleMail(to, "Default Mail", "Test Mail");
		}
	}
}
