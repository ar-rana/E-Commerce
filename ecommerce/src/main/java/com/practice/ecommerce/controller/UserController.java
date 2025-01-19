package com.practice.ecommerce.controller;

import java.util.List;
import java.util.Map;

import ch.qos.logback.core.joran.action.ActionUtil;
import com.practice.ecommerce.model.Enums.UserType;
import com.practice.ecommerce.model.Order;
import com.practice.ecommerce.model.User;
import com.practice.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/add/customer")
    public ResponseEntity<User> addCustomer(@RequestBody Map<String, String> user) {
        User customer = userService.addCustomer(user.get("user"), UserType.customer);
        if (customer !=  null) {
            return new ResponseEntity<>(customer, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/view")
    public ResponseEntity<User> getUser(@RequestBody Map<String, String> identifier) {
        User user = userService.getUserByIdentifier(identifier.get("identifier"));
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getOrders(@RequestBody Map<String, String> identifier) {
        List<Order> orders = userService.getCustomerOrders(identifier.get("identifier"));
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
}
