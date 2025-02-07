package com.practice.ecommerce.repository;

import java.util.List;

import com.practice.ecommerce.model.Review;
import com.practice.ecommerce.model.compositeId.ReviewId;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewsRepository extends JpaRepository<Review, ReviewId> {
    @Transactional
    @Query("SELECT r FROM Review r WHERE productId = ?1 ORDER BY RANDOM() LIMIT ?2")
    List<Review> findRandomReviews(Integer productId, Integer limit);

    @Transactional
    @Query("SELECT r FROM Review r WHERE productId = ?1 AND r.identifier NOT IN ?2 ORDER BY RANDOM() LIMIT ?3")
    List<Review> findNewReviews(Integer productId, List<String> identifiers, Integer limit);
}
