package com.practice.ecommerce.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.practice.ecommerce.model.Enums.EmailMessages;
import com.practice.ecommerce.service.EmailService;
import com.practice.ecommerce.service.OrderService;
import com.practice.ecommerce.service.ProductService;
import com.practice.ecommerce.service.redis.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email") // for testing purpose
public class EmailController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private Publisher publisher;

    @Value("${app.admin.mail}")
    public String to;

    @GetMapping("/welcome") // for testing
    public void sendWelcomeMail() {
        emailService.sendWelcomeMail(to, EmailMessages.welcomeMessage);
    }

    @GetMapping("/stock") // for testing
    public void sendProductMail() {
        emailService.sendHtmlMail(to, EmailMessages.productStockOver, "2");
    }

    @PostMapping("/feedback")
    public ResponseEntity<?> sendSimpleMail(@RequestBody Map<String, String> msg) {
        Map<String, String> map = new HashMap<>();
        map.put("type", EmailMessages.simpleMessage.name());
        map.put("to", to);
        map.put("subject", msg.get("subject"));
        map.put("content", msg.get("content"));
        publisher.publishToStream(map);
        return ResponseEntity.ok().build();
    }
}
