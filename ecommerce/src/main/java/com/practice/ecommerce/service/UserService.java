package com.practice.ecommerce.service;

import java.util.List;
import java.util.Optional;

import com.practice.ecommerce.model.Enums.UserType;
import com.practice.ecommerce.model.Order;
import com.practice.ecommerce.model.User;
import com.practice.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User addCustomer(String user, UserType userType) {
        User customer = new User(user, userType);
        return userRepository.save(customer);
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }

    public User getUserById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    public User getUserByIdentifier(String identifier) {
        return userRepository.findByIdentifier(identifier).orElse(null);
    }

    public User getUserObject(String identifier) {
        return userRepository.findByIdentifierAndGetOrder(identifier).orElse(null);
    }

    public List<Order> getCustomerOrders(String identifier) {
        Optional<User> user = userRepository.findByIdentifierAndGetOrder(identifier);
        System.out.println(user);
        if (user.isPresent()) {
            return user.get().getOrders();
        }
        return null;
    }

}
