package com.practice.ecommerce.repository;

import java.util.Optional;

import com.practice.ecommerce.model.Product;
import com.practice.ecommerce.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface StockRepository extends JpaRepository<Stock, Integer> {

    @Query("SELECT s FROM Stock s JOIN FETCH s.product WHERE s.productId = ?1")
    Optional<Stock> findByIdAndProduct(Integer id);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Stock s SET s.virtualStock = s.virtualStock + ?1 WHERE s.productId = ?2")
    Integer adjustVirtualStock(Integer amount, Integer productId);
}
