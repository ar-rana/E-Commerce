package com.practice.ecommerce.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.practice.ecommerce.model.Enums.EmailMessages;
import com.practice.ecommerce.model.Enums.Keys;
import com.practice.ecommerce.model.Enums.UserType;
import com.practice.ecommerce.model.Order;
import com.practice.ecommerce.model.User;
import com.practice.ecommerce.repository.UserRepository;
import com.practice.ecommerce.service.redis.Publisher;
import com.practice.ecommerce.service.redis.RedisCacheService;
import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RedisCacheService cache;

    @Autowired
    private Publisher publisher;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public String addCustomer(String user, UserType userType) { // cached
        String token;
        User existingUser = getUserByIdentifier(user);
        if (existingUser != null) {
            token = jwtService.generateToken(existingUser.getIdentifier(), UserType.customer);
            return token;
        }
        User customer = new User(user, userType);
        User savedUser = userRepository.save(customer);
        cache.setCache(Keys.key(Keys.USER, savedUser.getIdentifier()), savedUser, 100);
        token = jwtService.generateToken(savedUser.getIdentifier(), userType);

        Map<String, String> map = new HashMap<>();
        map.put("type", EmailMessages.welcomeMessage.name());
        map.put("to", user);
        publisher.publishToStream(map);

        cache.deleteCache(Keys.key(Keys.USER, user + "otp"));
        return token;
    }

    public void updateUser(User user) {
        userRepository.save(user);
        cache.deleteCache(Keys.key(Keys.USER, user.getIdentifier()));
    }

    public User getUserByIdentifier(String identifier) { // cached
        String key = Keys.key(Keys.USER, identifier);
        User item = cache.getCache(key, User.class);
        if (item != null) {
            logger.info("getUserByIdentifier from cacahe: {}", item);
            return item;
        }
        Optional<User> user = userRepository.findByIdentifier(identifier);
        if (user.isPresent()) {
            cache.setCache(key, user.get(), 100);
            return user.get();
        }
        return null;
    }

    public List<Order> getCustomerOrders(String identifier) { // cached
        String key = Keys.key(Keys.USER, identifier+"orders");
        List<Order> item = cache.getCache(key, new TypeReference<List<Order>>() {});
        if (item != null) {
            logger.info("getCustomerOrders from cacahe: {}", item);
            return item;
        }
        Optional<User> user = userRepository.findByIdentifierAndGetOrder(identifier);
        if (user.isPresent()) {
            cache.setCache(key, user.get().getOrders(), 20);
            return user.get().getOrders();
        }
        return null;
    }

    public User getUserObject(String identifier) {
        return userRepository.findByIdentifierAndGetOrder(identifier).orElse(null);
    }

    public User getUserById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    public boolean validateEmail(String identifier) {
        return EmailValidator.getInstance().isValid(identifier);
    }
}
