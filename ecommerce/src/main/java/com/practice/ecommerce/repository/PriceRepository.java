package com.practice.ecommerce.repository;

import com.practice.ecommerce.model.Price;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceRepository extends JpaRepository<Price, Integer> {
}
