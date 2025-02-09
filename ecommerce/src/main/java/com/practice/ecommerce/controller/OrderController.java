package com.practice.ecommerce.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.practice.ecommerce.model.Order;
import com.practice.ecommerce.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/new/{identifier}") // checked
    public ResponseEntity<?> newOrder(@PathVariable String identifier, @RequestBody Map<String, String> item) {
        String address = item.get("address");
        String contact = item.get("contact");
        String name = item.get("customer");
        log.info("customer: {}, contact: {}, address: {}", name, address, contact);
        Map<String, String> order = orderService.newOrder(identifier, address, contact, name);
        if (order == null) {
            return new ResponseEntity<>(order, HttpStatus.EXPECTATION_FAILED);
        }
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PostMapping("/success/{identifier}")
    public ResponseEntity<String> orderSuccess(@PathVariable String identifier, @RequestBody Map<String, String> item) {
        String razorpay_payment_id = item.get("razorpay_payment_id");
        String razorpay_order_id = item.get("razorpay_order_id");
        String razorpay_signature = item.get("razorpay_signature");
        String referenceId = item.get("referenceId");

        String msg = orderService.successHandler(razorpay_payment_id, razorpay_order_id, razorpay_signature, referenceId, identifier);
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @GetMapping("/get/{identifier}") // checked for each separate order
    public ResponseEntity<?> getOrders(@PathVariable String identifier, @RequestParam(required = false) Integer orderId) {
        if (orderId == null) {
            List<Order> orders = orderService.getOrders(identifier);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        }
        Order order = orderService.getOrderById(orderId);
        if (order != null) {
            return new ResponseEntity<>(order, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PostMapping("/getCost") // checked
    public ResponseEntity<Integer> getCost(@RequestBody Map<String, List<Integer>> list) {
        Integer total = orderService.getTotal(list.get("productId"));
        return new ResponseEntity<>(total, HttpStatus.OK);
    }

    @PostMapping("/review/{identifier}") // checked
    public ResponseEntity<String> addReview(@PathVariable String identifier, @RequestBody Map<String, String> review) {
        Integer productId = Integer.valueOf(review.get("productId"));
        Integer rating = Integer.valueOf(review.get("rating"));
        String content = review.get("review");
        orderService.addReview(productId, identifier, content, rating);
        return new ResponseEntity<>("ADDED REVIEW", HttpStatus.OK);
    }
}
