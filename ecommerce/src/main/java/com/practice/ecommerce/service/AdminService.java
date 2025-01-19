package com.practice.ecommerce.service;

import com.practice.ecommerce.controller.AdminController;
import com.practice.ecommerce.model.Price;
import com.practice.ecommerce.model.Product;
import com.practice.ecommerce.model.User;
import com.practice.ecommerce.repository.PriceRepository;
import com.practice.ecommerce.repository.ProductRepository;
import com.practice.ecommerce.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private PriceRepository priceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    public boolean addProduct(Product product) {
        return productRepository.save(product) != null;
    }

    public boolean changePrice(Price price) {
        return priceRepository.save(price) != null;
    }

    public boolean addAdmin(User user) {
        return userRepository.save(user) != null;
    }
}
