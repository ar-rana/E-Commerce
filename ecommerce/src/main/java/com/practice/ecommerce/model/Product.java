package com.practice.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.practice.ecommerce.model.Enums.ProductCategory;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    public Integer productId;

    public String name;
    public Integer basicPrice;
    public String thumbnail;
    public Integer stock;
    @Enumerated(EnumType.STRING)
    public ProductCategory category;

    // parent to 'Price'
    // we dont want to keep the price if the product is deleted,
    // so to keep both table tightly coupled we use 'CascadeType.ALL'
    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @PrimaryKeyJoinColumn // they share the same PK and PK of child is also foreign key to parent
    @JsonManagedReference // to avoid infinite recursion in bi-direction relation
    private Price price;

    public Product(String name, Integer basicPrice, String thumbnail, ProductCategory category, Integer stock) {
        this.name = name;
        this.basicPrice = basicPrice;
        this.thumbnail = thumbnail;
        this.category = category;
        this.stock = stock;
    }

    public Product() { }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public Integer getBasicPrice() {
        return basicPrice;
    }

    public void setBasicPrice(Integer basicPrice) {
        this.basicPrice = basicPrice;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", name='" + name + '\'' +
                ", basicPrice=" + basicPrice +
                ", thumbnail='" + thumbnail + '\'' +
                ", category='" + category + '\'' +
                ", stock=" + stock +
                ", price=" + price +
                '}';
    }
}
