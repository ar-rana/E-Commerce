package com.practice.ecommerce.service;

import java.util.List;

import com.practice.ecommerce.model.Enums.Keys;
import com.practice.ecommerce.model.Review;
import com.practice.ecommerce.repository.ReviewsRepository;
import com.practice.ecommerce.service.redis.RedisCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

@Service
public class ReviewService {

    @Autowired
    private ReviewsRepository reviewsRepository;

    @Autowired
    private RedisCacheService cache;

    public boolean addReview(Integer productId, String identifier, String review, Integer rating) {
        reviewsRepository.save(new Review(productId, identifier, review, rating));
        return true;
    }

    @GetMapping
    public List<Review> getReviews(Integer productId) {
        Integer limit = 5;
        String key = Keys.key(Keys.REVIEW, productId);
        List<Review> items = cache.getMatchers(key + "/*", Review.class, limit);
        if (items != null) {
            return items;
        }
        List<Review> reviews = reviewsRepository.findRandomReviews(productId, limit);
        for (Review review: reviews) {
            cache.setCache(key + "/" + review.getIdentifier(),  review, 30);
        }
        return reviews;
    }

    public List<Review> getNewReviews(Integer productId, List<String> reviewIds) {
        List<Review> reviews = getReviews(productId);
        boolean hasDuplicate = reviews.stream().anyMatch(review -> reviewIds.contains(review.getIdentifier()));
        if (hasDuplicate) {
            String key = Keys.key(Keys.REVIEW, productId);
            List<Review> reviewsFromRepo = reviewsRepository.findNewReviews(productId, reviewIds, 5);
            for (Review review: reviewsFromRepo) {
                cache.setCache(key + "/" + review.getIdentifier(),  review, 30);
            }
            return reviewsFromRepo;
        } else {
            return reviews;
        }
    }
}
