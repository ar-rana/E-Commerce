package com.practice.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.Check;

@Entity
@Table(name = "prices")
public class Price {

    @Id
    public Integer productId; // shares this with 'Product'
    public Integer currentPrice;

    // this is child entity to 'Product'
    @OneToOne
    @MapsId // maps PK of parent
    @JsonBackReference // to avoid infinite recursion in bi-direction relation
    public Product product;

    public Price(Product product, Integer currentPrice) {
        this.product = product;
        this.currentPrice = currentPrice;
    }

    public Price(Integer productId, Integer currentPrice) {
        this.productId = productId;
        this.currentPrice = currentPrice;
    }

    public Price() { }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(Integer currentPrice) throws Exception {
        if (this.currentPrice > product.basicPrice) {
            throw new Exception("Value Currrent Price less than Basic Price not allowed");
        }
        this.currentPrice = currentPrice;
    }
}
