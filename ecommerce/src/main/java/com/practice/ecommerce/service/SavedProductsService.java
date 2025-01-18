package com.practice.ecommerce.service;

import java.util.List;

import com.practice.ecommerce.controller.ProductController;
import com.practice.ecommerce.model.Enums.ListType;
import com.practice.ecommerce.model.ListId;
import com.practice.ecommerce.model.Product;
import com.practice.ecommerce.model.SavedProduct;
import com.practice.ecommerce.repository.SavedProductsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class SavedProductsService {

    @Autowired
    private SavedProductsRepo savedProductsRepo;

    @Autowired
    private ProductController productController;

    public Product addToList(ListId listId, Integer productId) {
        Product product = productController.getProduct(productId).getBody();
        if (product == null) return null;
        SavedProduct savedProduct = savedProductsRepo.findByIdentifierAndListType(listId.getIdentifier(), listId.getListType());

        if (savedProduct != null) {
            savedProduct.getProducts().add(product);
            savedProductsRepo.save(savedProduct);
        } else {
            SavedProduct newCustomer = new SavedProduct(listId.getIdentifier(), listId.getListType());
            newCustomer.getProducts().add(product);
            savedProductsRepo.save(newCustomer);
        }
        return product;
    }

    public List<Product> getListItems(ListType listType, String identifier) {
        SavedProduct savedProduct = savedProductsRepo.findByIdentifierAndListType(identifier, listType);
        if (savedProduct == null) {
            return null;
        }
        return savedProduct.getProducts();
    }

    public boolean deleteListItem(ListType listType, String identifier, Integer productId) {
        List<Product> products = getListItems(listType, identifier);
        if (products == null || products.isEmpty()) {
            return false;
        }
        SavedProduct savedProduct = new SavedProduct(identifier, listType);
        for (Product pr : products) {
            if (pr.getProductId() != productId) {
                savedProduct.getProducts().add(pr);
            }
        }
        savedProductsRepo.save(savedProduct);
        return true;
    }

//    public ResponseEntity<String> moveToCart();
}
