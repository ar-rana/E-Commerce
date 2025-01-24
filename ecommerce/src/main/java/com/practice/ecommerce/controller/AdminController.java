package com.practice.ecommerce.controller;

import java.util.Map;

import com.practice.ecommerce.model.Enums.ProductCategory;
import com.practice.ecommerce.model.Enums.UserType;
import com.practice.ecommerce.model.Stock;
import com.practice.ecommerce.model.Product;
import com.practice.ecommerce.model.User;
import com.practice.ecommerce.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
//Demo Product format
// {
//    "name": "someName",
//    "basicPrice": 500,
//    "thumbnail": "base64",
//    "category": "homedecore",
//    "stock": 100,
//    "currentPrice": 450,
//  }
    @PostMapping("/add/product") // checked
    public ResponseEntity<String> addProduct(@RequestBody Map<String, String> item) {
        int stock = Integer.parseInt(item.get("stock"));
        Product product = new Product(
                item.get("name"),
                Integer.valueOf(item.get("basicPrice")),
                Integer.valueOf(item.get("currentPrice")),
                item.get("thumbnail"),
                stock,
                ProductCategory.valueOf(item.get("category"))
        );
        Stock virtualStock = new Stock(product, stock-10);
        product.setVirtualStock(virtualStock);
        Product newProduct = adminService.addProduct(product);
        if (newProduct != null) return new ResponseEntity<>("Product saved successfully!! " + newProduct.toString(), HttpStatus.OK);
        return new ResponseEntity<>("Product saved successfully!!" + product.toString(), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/add/admin") // checked
    public ResponseEntity<User> addAdmin(@RequestBody Map<String, String> user) {
        User admin = adminService.addAdmin(user.get("user"), UserType.admin);
        if (admin !=  null) {
            logger.info("NEW ADMIN: " + admin.toString());
            return new ResponseEntity<>(admin, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/alter/price") // checked
    public String changePrice(@RequestBody Map<String, Integer> newPrice) {
        Integer setPrice = newPrice.get("currentPrice");
        if (adminService.changePrice(setPrice, newPrice.get("productId"))) return "Changed price to " + setPrice;
        return "Failed to alter price for product: " + newPrice.get("productId");
    }
}
