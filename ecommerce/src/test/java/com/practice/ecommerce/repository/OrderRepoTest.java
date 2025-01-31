package com.practice.ecommerce.repository;

import java.util.Optional;

import com.practice.ecommerce.defaultModels.DefaultModels;
import com.practice.ecommerce.model.Enums.DeliveryStatus;
import com.practice.ecommerce.model.Order;
import com.practice.ecommerce.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Rollback
public class OrderRepoTest {

    @Autowired
    private OrderRepository orderRepository;

    private Product product = DefaultModels.product;
//    private Stock stock = DefaultModels.virtualStock;
    private Order order;
    Order savedOrder;

    @BeforeEach
    public void setUp() {
//        product.setVirtualStock(stock);
        order = new Order(DefaultModels.username, DeliveryStatus.pending, product);

        savedOrder = orderRepository.save(order);
    }

    @Test
    public void testSave() {
        assertEquals(order, savedOrder);
    }
    @Test
    public void testFindById() {
        Optional<Order> newOrder = orderRepository.findById(savedOrder.getOrderId());

        assertTrue(newOrder.isPresent());
        assertEquals(savedOrder, newOrder.get());
    }
}
