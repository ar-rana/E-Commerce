package com.practice.ecommerce.controller;

import java.util.Map;

import com.practice.ecommerce.model.Cart;
import com.practice.ecommerce.model.Product;
import com.practice.ecommerce.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public ResponseEntity<Cart> getCart(@RequestBody Map<String, String> identifier) {
        Cart cart = cartService.getCart(identifier.get("identifier"));
        if (cart == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Product> addToCart(@RequestBody Map<String, String> item) {
        Product product = cartService.addToCart(item.get("identifier"), Integer.valueOf(item.get("productId")));
        if (product != null) {
            return new ResponseEntity<>(product, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> deleteListItem(@RequestBody Map<String, String> item) {
        if (cartService.deleteItemFromCart(item.get("identifier"), Integer.valueOf(item.get("productId")))) {
            return new ResponseEntity<>("DELETED", HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
}
