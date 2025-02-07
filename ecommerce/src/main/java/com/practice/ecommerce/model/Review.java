package com.practice.ecommerce.model;

import com.practice.ecommerce.model.compositeId.ReviewId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "reviews")
@NoArgsConstructor
@Getter
@Setter
@IdClass(ReviewId.class)
public class Review {

    @Id
    @Column(nullable = false)
    public Integer productId;
    @Id
    @Column(nullable = false)
    public String identifier;

    @Column(columnDefinition = "TEXT")
    public String review;
    @Column(nullable = false)
    public Integer rating;

    public Review(Integer productId, String identifier, String review, Integer rating) {
        this.productId = productId;
        this.identifier = identifier;
        this.review = review;
        this.rating = rating;
    }

    public Review(Integer productId, String identifier, Integer rating) {
        this.productId = productId;
        this.identifier = identifier;
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Review{" +
                "productId=" + productId +
                ", identifier=" + identifier +
                ", review='" + review + '\'' +
                ", rating=" + rating +
                '}';
    }
}
