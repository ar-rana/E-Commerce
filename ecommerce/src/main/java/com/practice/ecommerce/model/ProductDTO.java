package com.practice.ecommerce.model;

import com.practice.ecommerce.model.Enums.ProductCategory;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@Getter
@Setter
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

}
