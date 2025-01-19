package com.practice.ecommerce.service;

import java.util.List;
import java.util.Optional;

import com.practice.ecommerce.model.Enums.ProductCategory;
import com.practice.ecommerce.model.Price;
import com.practice.ecommerce.model.Product;
import com.practice.ecommerce.model.User;
import com.practice.ecommerce.repository.PriceRepository;
import com.practice.ecommerce.repository.ProductRepository;
import com.practice.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PriceRepository priceRepository;

    public Product getProduct(Integer id) {
        Optional<Product> product = productRepository.findById(id);
        return product.orElse(null);
    }

    public Price getPriceProduct(Integer id) {
        Optional<Price> price = priceRepository.findByIdAndProduct(id);
        return  price.orElse(null);
    }

    public Price getPriceOnly(Integer id) {
        Optional<Price> price = priceRepository.findById(id);
        System.out.println(getPriceProduct(id).getProduct().toString()); // checked, works
        return  price.orElse(null);
    }

    public List<Product> getProductByCategory(ProductCategory category) {
        return productRepository.findByCategory(category);
    }
}
