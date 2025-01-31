package com.practice.ecommerce.service;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.practice.ecommerce.model.Enums.Keys;
import com.practice.ecommerce.model.Enums.UserType;
import com.practice.ecommerce.model.Order;
import com.practice.ecommerce.model.User;
import com.practice.ecommerce.repository.UserRepository;
import com.practice.ecommerce.service.redis.RedisCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedisCacheService cache;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public User addCustomer(String user, UserType userType) { // cached
        User customer = new User(user, userType);
        User savedUser = userRepository.save(customer);
        cache.setCache(Keys.key(Keys.USER, savedUser.getIdentifier()), savedUser, 10);
        return savedUser;
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
            cache.setCache(key, user.get(), 10);
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
            cache.setCache(key, user.get().getOrders(), 10);
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
}
