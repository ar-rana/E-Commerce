package com.practice.ecommerce.config;

import com.practice.ecommerce.service.redis.Receiver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    // this bean is so that the serializers of redis and springBoot are the same, otherwise there will be conflict
    // in the serializers, the value of SpringBoot will not be available in redis cli at 6379
    @Bean // beans are autodetect by redis and invoked, all values passed in the parameters of bean are auto-injected
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        // key and value will both be saved as Strings

        return redisTemplate;
    }

    @Bean
	ChannelTopic topic() {
		return new ChannelTopic("notification");
	}

	@Bean
	RedisMessageListenerContainer redisContainer(RedisConnectionFactory connectionFactory) {
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.addMessageListener(new MessageListenerAdapter(new Receiver()), topic());

		return container;
	}
}
