package com.practice.ecommerce.service;

import com.practice.ecommerce.model.Enums.UserType;
import com.practice.ecommerce.model.Stock;
import com.practice.ecommerce.model.Product;
import com.practice.ecommerce.model.User;
import com.practice.ecommerce.repository.StockRepository;
import com.practice.ecommerce.repository.ProductRepository;
import com.practice.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    public boolean changePrice(Integer price, Integer productId) {
        return productRepository.alterCurrentPrice(price, productId) > 0;
    }

    public User addAdmin(String user, UserType userType) {
        User admin = new User(user, userType);
        return userRepository.save(admin);
    }
}
