package com.practice.ecommerce.repository;

import java.util.List;

import com.practice.ecommerce.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ImagesRepository extends JpaRepository<Image, Integer> {

    @Query("SELECT i.imageId FROM Image i WHERE i.productId = ?1")
    List<Integer> findByProductIdAndGetImageId(Integer productId);
}
