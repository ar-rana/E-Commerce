package com.practice.ecommerce.repository;

import java.util.List;
import java.util.Optional;

import com.practice.ecommerce.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByUserIdentifier(String identifier);

    @Query("SELECT o FROM Order o WHERE o.userIdentifier = ?1 AND o.product.productId IN ?2")
    List<Order> findOrderForUser(String identifier, List<Integer> productIds);
}
