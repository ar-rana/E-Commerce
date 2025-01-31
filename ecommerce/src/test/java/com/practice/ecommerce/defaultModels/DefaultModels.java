package com.practice.ecommerce.defaultModels;

import com.practice.ecommerce.model.Enums.ProductCategory;
import com.practice.ecommerce.model.Product;
import org.springframework.stereotype.Component;

@Component
public class DefaultModels {

    public static final Integer DEFAULT_ID = 235115;

    public static final String productName1 = "Rock";
    public static final Integer basicPrice1 = 1000;
    public static final Integer currentPrice1 = 750;
    public static final String productName2 = "Stone";
    public static final Integer basicPrice2= 2000;
    public static final Integer currentPrice2 = 950;
    public static final String thumbnail = "zxyxyxyxyxyxyz";
    public static Integer stock = 100;

    public static final String username = "example@email.com";

    public static Product product = new Product(
            DefaultModels.productName1,
            DefaultModels.basicPrice1,
            DefaultModels.currentPrice1,
            DefaultModels.thumbnail,
            DefaultModels.stock,
            ProductCategory.homedecore,
            stock - 10,
            "PNG"
    );
    public static Product alternateProduct = new Product(
            DefaultModels.productName2,
            DefaultModels.basicPrice2,
            DefaultModels.currentPrice2,
            DefaultModels.thumbnail,
            DefaultModels.stock,
            ProductCategory.outdoordecore,
            stock - 10,
            "PNG"
    );
}
