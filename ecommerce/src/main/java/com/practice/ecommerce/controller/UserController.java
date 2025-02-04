package com.practice.ecommerce.controller;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.practice.ecommerce.model.Enums.EmailMessages;
import com.practice.ecommerce.model.Enums.Keys;
import com.practice.ecommerce.model.Enums.UserType;
import com.practice.ecommerce.model.Order;
import com.practice.ecommerce.model.User;
import com.practice.ecommerce.service.EmailService;
import com.practice.ecommerce.service.UserService;
import com.practice.ecommerce.service.redis.RedisCacheService;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private RedisCacheService cache;

    @GetMapping("/request-otp/{identifier}") // checked
    public ResponseEntity<String> requestOTP(@PathVariable String identifier) {
        if (!userService.validateEmail(identifier)) {
            return new ResponseEntity<>("Invalid Email!!", HttpStatus.BAD_REQUEST);
        }
        String otp = generateOtp();
        emailService.sendOTPMail(identifier, EmailMessages.otpRequest, otp);
        cache.setCache(Keys.key(Keys.USER, identifier + "otp"), otp, 10);
        return new ResponseEntity<>("Please Check You Email!!", HttpStatus.OK);
    }

    @PostMapping("/customer") // checked
    public ResponseEntity<String> addCustomer(@RequestBody Map<String, String> user) {
        String identifier = user.get("identifier");
        String otp = user.get("otp");
        if (!userService.validateEmail(identifier)) {
            return new ResponseEntity<>("Invalid Email!!", HttpStatus.BAD_REQUEST);
        }
        String item = cache.getCache(Keys.key(Keys.USER, identifier + "otp"), new TypeReference<String>() {});
        if (item == null || !item.equals(otp)) {
            return new ResponseEntity<>("Invalid Credentials", HttpStatus.BAD_REQUEST);
        }
        String token = userService.addCustomer(identifier, UserType.customer);
        if (token !=  null) {
            cache.deleteCache(Keys.key(Keys.USER, identifier + "otp"));
            return new ResponseEntity<>(token, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/view/{identifier}") // checked
    public ResponseEntity<User> getUser(@PathVariable String identifier) {
        User user = userService.getUserByIdentifier(identifier);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/orders/{identifier}") // checked
    public ResponseEntity<List<Order>> getOrders(@PathVariable String identifier) {
        List<Order> orders = userService.getCustomerOrders(identifier);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    public String generateOtp() {
        StringBuilder sb = new StringBuilder();
        for (int i=0;i<6;i++) {
            sb.append((int) (Math.random() * 10));
        }
        return sb.toString();
    }
}
