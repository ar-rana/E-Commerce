package com.practice.ecommerce.repository;

import java.util.List;
import java.util.Optional;

import com.practice.ecommerce.model.Enums.ProductCategory;
import com.practice.ecommerce.model.Product;
import com.practice.ecommerce.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByCategory(ProductCategory category);

    @Query("SELECT p.currentPrice FROM Product p WHERE p.productId = ?1")
    Optional<Integer> findCurrentPriceById(Integer id);

    @Query("SELECT p.currentPrice FROM Product p WHERE p.productId IN :ids")
    List<Integer> findAllByIdAndGetCurrentPrice(Iterable<Integer> ids);

    // clearAutomatically clears the cache JPA has kept of the product
    @Modifying(clearAutomatically = true) // needed for update/delete to tell JPA this is not a select query
    @Query("UPDATE Product p SET p.currentPrice = ?1 WHERE p.productId = ?2")
    Integer alterCurrentPrice(Integer price, Integer productId);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Product p SET p.stock = p.stock + ?1 WHERE p.productId = ?2")
    Integer adjustStock(Integer amount, Integer productId);
}
