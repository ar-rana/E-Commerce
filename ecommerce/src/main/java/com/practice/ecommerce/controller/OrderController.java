package com.practice.ecommerce.controller;

import java.util.List;
import java.util.Map;

import com.practice.ecommerce.model.Order;
import com.practice.ecommerce.service.OrderService;
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
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/new")
    public String newOrder(@RequestBody Map<String, String> item) {
        if (orderService.newOrder(item.get("identifier"), Integer.valueOf(item.get("productId")))) {
            return "Order Received!";
        }
        return "Error Processing Order!!";
    }

    @GetMapping("/get")
    public ResponseEntity<Order> getOrders(@RequestParam Integer orderId) {
        Order order = orderService.getOrders(orderId);
        if (order != null) {
            return new ResponseEntity<>(order, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/getCost")
    public ResponseEntity<Integer> getCost(@RequestBody Map<String, List<Integer>> list) {
        Integer total = orderService.getTotal(list.get("productId"));
        return new ResponseEntity<>(total, HttpStatus.NOT_FOUND);
    }
}
