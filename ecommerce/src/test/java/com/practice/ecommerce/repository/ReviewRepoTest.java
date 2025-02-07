package com.practice.ecommerce.repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.practice.ecommerce.defaultModels.DefaultModels;
import com.practice.ecommerce.model.Review;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Rollback
public class ReviewRepoTest {

    @Autowired
    private ReviewsRepository reviewsRepository;

    Review review1 = new Review(1, DefaultModels.username, "Good Product", 4);
    Review review2 = new Review(1, "test@testing.in", 4);
    Review savedReview1;
    Review savedReview2;
    Set<Review> set;

    @BeforeEach
    public void setUp() {
        savedReview1 = reviewsRepository.save(review1);
        savedReview2 = reviewsRepository.save(review2);

        set = Set.of(savedReview1, savedReview2);
    }

    @Test
    public void testSave() {
        assertTrue(new ReflectionEquals(review1).matches(savedReview1));
    }

    @Test
    public void testFindRandomReviews() {
        List<Review> reviews = reviewsRepository.findRandomReviews(1, 2);
        List<Review> noReviews = reviewsRepository.findRandomReviews(2, 1);

        assertFalse(reviews.isEmpty());
        assertTrue(noReviews.isEmpty());
    }

    @Test
    public void testFindNewReviews() {
        List<Review> reviews = reviewsRepository.findRandomReviews(1, 1);
        String id = reviews.getFirst().getIdentifier();
        List<Review> newReview = reviewsRepository.findNewReviews(1, List.of(id), 1);

        assertFalse(reviews.isEmpty());
        assertFalse(newReview.isEmpty());
        assertFalse(new ReflectionEquals(reviews.getFirst()).matches(newReview.getFirst()));
    }

}
