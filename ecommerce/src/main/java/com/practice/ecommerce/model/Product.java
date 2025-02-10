package com.practice.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.google.firebase.database.annotations.NotNull;
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "products")
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    public Integer productId;

    @NotNull
    public String name;
    @NotNull
    public Integer basicPrice;
    public Integer currentPrice;
    @NotNull
    public Integer stock;
    @Enumerated(EnumType.STRING)
    public ProductCategory category;
    public Integer virtualStock;
    public String thumbnailType;
    @Column(columnDefinition = "TEXT")
    public String thumbnail;

    // parent to 'Price'
    // we dont want to keep the price if the product is deleted,
    // so to keep both table tightly coupled we use 'CascadeType.ALL'
//    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    @PrimaryKeyJoinColumn // they share the same PK and PK of child is also foreign key to parent
//    @JsonManagedReference // to avoid infinite recursion in bi-direction relation
//    private Stock virtualStock;

    public Product(String name, Integer basicPrice, Integer currentPrice, String thumbnail, Integer stock, ProductCategory category) {
        this.name = name;
        this.basicPrice = basicPrice;
        this.currentPrice = currentPrice;
        this.thumbnail = thumbnail;
        this.stock = stock;
        this.category = category;
    }

    public Product(String name, Integer basicPrice, Integer currentPrice, String thumbnail, Integer stock, ProductCategory category, Integer virtualStock, String thumbnailType) {
        this.name = name;
        this.basicPrice = basicPrice;
        this.currentPrice = currentPrice;
        this.thumbnail = thumbnail;
        this.stock = stock;
        this.category = category;
        this.virtualStock = virtualStock;
        this.thumbnailType = thumbnailType;
    }

    public Product() { }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", customer='" + name + '\'' +
                ", basicPrice=" + basicPrice +
                ", currentPrice=" + currentPrice +
                ", thumbnail='" + thumbnail + '\'' +
                ", stock=" + stock +
                ", category=" + category +
                ", virtualStock=" + virtualStock +
                '}';
    }
}
