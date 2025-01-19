package com.practice.ecommerce.repository;

import java.util.Optional;

import com.practice.ecommerce.model.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PriceRepository extends JpaRepository<Price, Integer> {

    @Query("SELECT p FROM Price p JOIN FETCH p.product WHERE p.productId = ?1")
    Optional<Price> findByIdAndProduct(Integer id);
}
