package com.practice.ecommerce.model;

import com.practice.ecommerce.model.Enums.ProductCategory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ProductDTO {
    private String name;
    private Integer basicPrice;
    private Integer currentPrice;
    private Integer stock;
    private ProductCategory category;
    private MultipartFile thumbnail;
    private String tags;

    public ProductDTO(String name, Integer basicPrice, Integer currentPrice, Integer stock, ProductCategory category, MultipartFile thumbnail) {
        this.name = name;
        this.basicPrice = basicPrice;
        this.currentPrice = currentPrice;
        this.stock = stock;
        this.category = category;
        this.thumbnail = thumbnail;
    }

    public ProductDTO() { }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBasicPrice() {
        return basicPrice;
    }

    public void setBasicPrice(Integer basicPrice) {
        this.basicPrice = basicPrice;
    }

    public Integer getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(Integer currentPrice) {
        this.currentPrice = currentPrice;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public MultipartFile getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(MultipartFile thumbnail) {
        this.thumbnail = thumbnail;
    }
}
