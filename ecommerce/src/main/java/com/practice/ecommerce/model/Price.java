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
    public Integer productId; // shares this with 'Product'
    public Integer currentPrice;

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

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(Integer currentPrice) {
        this.currentPrice = currentPrice;
    }
}
