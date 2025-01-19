package com.practice.ecommerce.service;

import java.util.List;
import java.util.Optional;

import com.practice.ecommerce.model.Enums.DeliveryStatus;
import com.practice.ecommerce.model.Order;
import com.practice.ecommerce.model.Product;
import com.practice.ecommerce.model.User;
import com.practice.ecommerce.repository.OrderRepository;
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

    public boolean newOrder(String identifier, Integer productId) {
        Product product = productService.getProduct(productId);
        if (product == null) return false;
        User user = userService.getUserByIdentifier(identifier);
        if (user == null) return false;

        Order order = new Order(identifier, DeliveryStatus.pending, product);
        orderRepository.save(order);

        user.getOrders().add(order);
        userService.updateUser(user);

        return true;
    }

    public Order getOrders(Integer orderId, String identifier) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent()) {
            boolean belongsToUser = order.get().getUserIdentifier().equals(identifier);
            if (belongsToUser) {
                return order.get();
            }
            return null;
        }
        return null;
    }

    public Integer getTotal(List<Integer> productIds) {
        Integer total = 0;
        for (Integer i : productIds) {
            total += productService.getPriceOnly(i).currentPrice;
        }
        return total;
    }
}
