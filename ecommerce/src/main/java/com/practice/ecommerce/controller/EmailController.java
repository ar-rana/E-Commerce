package com.practice.ecommerce.controller;

import java.util.ArrayList;
import java.util.Arrays;

import com.practice.ecommerce.model.Enums.EmailMessages;
import com.practice.ecommerce.service.EmailService;
import com.practice.ecommerce.service.OrderService;
import com.practice.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    public String to = "aryan.rana2005@gmail.com";

    @GetMapping("/welcome")
    public void sendSimpleMail() {
        emailService.sendWelcomeMail(to, EmailMessages.welcomeMessage);
    }

    @GetMapping("/order")
    public void sendOrderMail() {
        emailService.sendMailWithAttachment(to, EmailMessages.orderPlaced, new ArrayList<>(Arrays.asList(orderService.getOrderById(1))));
    }

    @GetMapping("/stock")
    public void sendProductMail() {
        emailService.sendHtmlMail(to, EmailMessages.productStockOver, productService.getProduct(2));
    }
}
