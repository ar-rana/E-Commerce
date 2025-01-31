package com.practice.ecommerce.service;

import java.util.List;
import java.util.Optional;

import com.practice.ecommerce.model.Enums.DeliveryStatus;
import com.practice.ecommerce.model.Enums.Keys;
import com.practice.ecommerce.model.Order;
import com.practice.ecommerce.model.Product;
import com.practice.ecommerce.model.User;
import com.practice.ecommerce.repository.OrderRepository;
import com.practice.ecommerce.service.redis.RedisCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisCacheService cache;

    private static final Logger logger = LoggerFactory.getLogger(Order.class);

    public boolean newOrder(String identifier, Integer productId) {
        Product product = productService.getProduct(productId);
        if (product == null) return false;
        User user = userService.getUserObject(identifier);
        logger.info("Product: {} + User: {}", product, user);
        if (user == null) return false;

        Order order = new Order(identifier, DeliveryStatus.pending, product);
        orderRepository.save(order);

        user.getOrders().add(order);
        userService.updateUser(user);

        cache.deleteCache(Keys.key(Keys.USER, identifier+"orders"));
        return true;
    }

    public Order getOrders(Integer orderId, String identifier) { // cached
        String key = Keys.key(Keys.ORDER, identifier);
        Order item = cache.getCache(key, Order.class);
        if (item != null) {
            logger.info("Item from cache PRODUCT: {} - key: {}", item, key);
            boolean belongsToUser = item.getUserIdentifier().equals(identifier);
            if (belongsToUser) {
                return item;
            }
            return null;
        }
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent()) {
            boolean belongsToUser = order.get().getUserIdentifier().equals(identifier);
            if (belongsToUser) {
                cache.setCache(key, order.get(),10);
                return order.get();
            }
            return null;
        }
        return null;
    }

    public Integer getTotal(List<Integer> productIds) {
        return productService.getTotalPrice(productIds);
    }

    public Order getOrderById(Integer orderId) { // cached
        String key = Keys.key(Keys.ORDER, orderId);
        Order item = cache.getCache(key, Order.class);
        if (item != null) {
            logger.info("Item from cache PRODUCT: {} - key: {}", item, key);
            return item;
        }
        Order order = orderRepository.findById(orderId).orElse(null);
        cache.setCache(key, order, 10);
        return null;
    }
}
