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

    @Autowired
    private UserRepository userRepository;

    public boolean addProduct(Product product) {
        return productRepository.save(product) != null;
    }

    public Product getProduct(Integer id) {
        Optional<Product> product = productRepository.findById(id);
        return product.orElse(null);
    }

    public Price getPrice(Integer id) {
        Optional<Price> price = priceRepository.findById(id);
        return  price.orElse(null);
    }

    public boolean changePrice(Price price) {
        return priceRepository.save(price) != null;
    }

    public boolean addAdmin(User user) {
        return userRepository.save(user) != null;
    }

    public List<Product> getProductByCategory(ProductCategory category) {
        return productRepository.findByCategory(category);
    }
}
