package com.practice.ecommerce.repository;

import java.util.List;
import java.util.Optional;

import com.practice.ecommerce.defaultModels.DefaultModels;
import com.practice.ecommerce.model.Enums.DeliveryStatus;
import com.practice.ecommerce.model.Enums.ProductCategory;
import com.practice.ecommerce.model.Order;
import com.practice.ecommerce.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class OrderRepoTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    Product product = new Product(
            DefaultModels.productName1,
            DefaultModels.basicPrice1,
            DefaultModels.currentPrice1,
            DefaultModels.thumbnail,
            DefaultModels.stock,
            ProductCategory.homedecore,
            DefaultModels.stock - 10,
            "PNG"
    );
    private Order order;
    private Order savedOrder;

    @BeforeEach
    public void setUp() {
        Product savedProduct = productRepository.save(product);

        order = new Order(DefaultModels.username, DeliveryStatus.pending, savedProduct);

        savedOrder = orderRepository.save(order);
    }

    @Test
    public void testSave() {
        assertTrue(new ReflectionEquals(order, "orderId").matches(savedOrder));
    }

    @Test
    public void testFindById() {
        Optional<Order> newOrder = orderRepository.findById(savedOrder.getOrderId());

        assertTrue(newOrder.isPresent());
        assertTrue(new ReflectionEquals(order, "orderId", "productId").matches(newOrder.get()));
    }

    @Test
    public void testFindByUserIdentifier() {
        List<Order> orders = orderRepository.findByUserIdentifier(order.getUserIdentifier());

        assertFalse(orders.isEmpty());
        assertEquals(1, orders.size());
        assertIterableEquals(List.of(order), orders);
    }
}
