package com.practice.ecommerce.controller;

import java.util.List;

import com.practice.ecommerce.model.Enums.ProductCategory;
import com.practice.ecommerce.model.Stock;
import com.practice.ecommerce.model.Product;
import com.practice.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/getProduct") // checked
    public ResponseEntity<Product> getProduct(@RequestParam Integer id) {
        Product product = productService.getProduct(id);
        if (product == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping("/getProduct/{category}") // checked
    public ResponseEntity<List<Product>> getProductByCategory(@PathVariable ProductCategory category) {
        List<Product> product = productService.getProductByCategory(category);
        if (product == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping("/getPrice") // checked
    public ResponseEntity<Integer> getPrice(@RequestParam Integer id) {
        Integer price = productService.getPriceOnly(id);
        if (price == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(price, HttpStatus.OK);
    }
}
