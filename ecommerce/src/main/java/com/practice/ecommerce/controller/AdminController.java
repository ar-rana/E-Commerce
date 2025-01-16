package com.practice.ecommerce.controller;

import java.util.Optional;

import com.practice.ecommerce.model.Price;
import com.practice.ecommerce.model.Product;
import com.practice.ecommerce.repository.ProductRepository;
import com.practice.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("/admin")
public class AdminController {

    @Autowired
    private ProductService productService;

//    {  Demo Product format
//    "name": "someName",
//    "basicPrice": 500,
//    "thumbnail": "xyzxyzxyzxyzxyz",
//    "category": "homedecore",
//    "stock": 100,
//    "price": {
//        "currentPrice": 450
//      }
//    }
    @PostMapping
    public String addProduct(@RequestBody Product product) {
        if (productService.addProduct(product)) return "Product saved successfully!!";
        return "Failed to add: " + product.toString();
    }

    @PostMapping
    public String changePrice(@RequestBody Price price) {
        if (productService.changePrice(price)) return "Changed price to " + price;
        return "Failed to alter price for product: " + price.getProductId();
    }

    @GetMapping
    public ResponseEntity<Product> getProduct(@RequestParam Integer id) {
        Product product = productService.getProduct(id);
        if (product == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Price> getPrice(@RequestParam Integer id) {
        Price price = productService.getPrice(id);
        if (price == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(price, HttpStatus.OK);
    }
}
