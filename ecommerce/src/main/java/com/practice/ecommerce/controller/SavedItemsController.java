package com.practice.ecommerce.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.practice.ecommerce.model.Enums.ListType;
import com.practice.ecommerce.model.ListId;
import com.practice.ecommerce.model.Product;
import com.practice.ecommerce.model.SavedProduct;
import com.practice.ecommerce.repository.SavedProductsRepo;
import com.practice.ecommerce.service.SavedProductsService;
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
@RequestMapping("/list")
public class SavedItemsController {

    @Autowired
    private SavedProductsRepo savedProductsRepo;

    @Autowired
    private SavedProductsService savedProductsService;

    @PostMapping("/add/{listType}")
    public ResponseEntity<Product> addToWishlist(@PathVariable ListType listType, @RequestBody Map<String, String> addProduct) {
        ListId listId = new ListId(addProduct.get("identifier"), listType);
        Product product = savedProductsService.addToList(listId, Integer.valueOf(addProduct.get("productId")));
        if (product == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        }
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping("/{listType}")
    public ResponseEntity<List<Product>> getWishlist(@PathVariable ListType listType, @RequestBody Map<String, String> identifier) {
        List<Product> products = savedProductsService.getListItems(listType, identifier.get("identifier"));
        if (products == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}
