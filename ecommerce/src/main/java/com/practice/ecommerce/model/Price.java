package com.practice.ecommerce.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "prices")
public class Price {

    @Id
    public int productId;
    public int currentPrice;

    // this is child entity to 'Product'
    @OneToOne
    @MapsId
    @JoinColumn(name = "product_id")
    public Product product;

    public Price(int productId, int currentPrice) {
        this.productId = productId;
        this.currentPrice = currentPrice;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(int currentPrice) {
        this.currentPrice = currentPrice;
    }
}
