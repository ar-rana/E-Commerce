package com.practice.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "stock")
public class Stock {

    @Id
    public Integer productId; // shares this with 'Product'
    public Integer virtualStock;

    // this is child entity to 'Product'
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // maps PK of parent
    @JsonBackReference // to avoid infinite recursion in bi-direction relation
    public Product product;

    public Stock(Product product, Integer virtualStock) {
        this.product = product;
        this.virtualStock = virtualStock;
    }

    public Stock(Integer productId, Integer virtualStock) {
        this.productId = productId;
        this.virtualStock = virtualStock;
    }

    public Stock() { }

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

    public Integer getVirtualStock() {
        return virtualStock;
    }

    public void setVirtualStock(Integer virtualStock) throws Exception {
        if (this.virtualStock > product.basicPrice) {
            throw new Exception("Value Currrent Price less than Basic Price not allowed");
        }
        this.virtualStock = virtualStock;
    }

    @Override
    public String toString() {
        return "Price{" +
                "productId=" + productId +
                ", virtualStock=" + virtualStock +
                '}';
    }
}
