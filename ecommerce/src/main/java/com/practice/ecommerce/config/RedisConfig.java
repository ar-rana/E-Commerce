package com.practice.ecommerce.config;

import java.awt.print.Book;
import java.time.Duration;

import com.practice.ecommerce.service.redis.RedisStreamListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;

@Configuration
public class RedisConfig {

    // this bean is so that the serializers of redis and springBoot are the same, otherwise there will be conflict
    // in the serializers, the value of SpringBoot will not be available in redis cli at 6379
    @Bean // beans are autodetect by redis and invoked, all values passed in the parameters of bean are auto-injected
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        // key and value will both be saved as Strings

        return redisTemplate;
    }
//    Streams
    @Bean(initMethod = "start", destroyMethod = "stop") // destroyMethod to shut down the stream before closing the app, and avoid 'Connection is already closed' exception
    public StreamMessageListenerContainer streamMessageListenerContainer(RedisConnectionFactory connectionFactory, RedisStreamListener redisStreamListener) {
        StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, MapRecord<String, String, String>> containerOptions =
                StreamMessageListenerContainer.StreamMessageListenerContainerOptions.builder().pollTimeout(Duration.ofMillis(2000)).build();

        StreamMessageListenerContainer<String, MapRecord<String, String, String>> container =
                StreamMessageListenerContainer.create(connectionFactory, containerOptions);

        container.receive(StreamOffset.fromStart("notification"), redisStreamListener);
        container.start(); // necessary to start the container

        return container;
    }
}
