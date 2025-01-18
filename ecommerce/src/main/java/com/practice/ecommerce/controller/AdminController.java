package com.practice.ecommerce.controller;

import java.util.Map;
import java.util.Optional;

import com.practice.ecommerce.model.Enums.ProductCategory;
import com.practice.ecommerce.model.Enums.UserType;
import com.practice.ecommerce.model.Price;
import com.practice.ecommerce.model.Product;
import com.practice.ecommerce.model.User;
import com.practice.ecommerce.repository.ProductRepository;
import com.practice.ecommerce.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ProductService productService;

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

//    {  Demo Product format
//    "name": "someName",
//    "basicPrice": 500,
//    "thumbnail": "xyzxyzxyzxyzxyz",
//    "category": "homedecore",
//    "stock": 100,
//    "currentPrice": 450
//    }
    @PostMapping("/add/product")
    public ResponseEntity<String> addProduct(@RequestBody Map<String, String> item) {
        Product product = new Product(
                item.get("name"),
                Integer.valueOf(item.get("basicPrice")),
                item.get("thumbnail"),
                ProductCategory.valueOf(item.get("category")),
                Integer.valueOf(item.get("stock"))
        );
        Price price = new Price(product, Integer.valueOf(item.get("currentPrice")));
        product.setPrice(price);
        if (productService.addProduct(product)) return new ResponseEntity<>("Product saved successfully!!" + product.toString(), HttpStatus.OK);
        return new ResponseEntity<>("Product saved successfully!!" + product.toString(), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/add/admin")
    public String addAdmin(@RequestBody Map<String, String> user) {
        User customer = new User(user.get("user"), UserType.admin);
        if (productService.addAdmin(customer)) {
            logger.info("NEW ADMIN: " + customer.getIdentifier());
            return customer.toString() + " added successfully!!";
        }
        return "Failed to add: " + customer.toString();
    }

    @PostMapping("/alter/price")
    public String changePrice(@RequestBody Map<String, Integer> newPrice) {
        Price price = new Price(newPrice.get("productId"), newPrice.get("currentPrice"));
        if (productService.changePrice(price)) return "Changed price to " + price;
        return "Failed to alter price for product: " + price.getProductId();
    }
}
