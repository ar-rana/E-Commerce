package com.practice.ecommerce.service;

import java.util.List;
import java.util.Optional;

import com.practice.ecommerce.model.Enums.ListType;
import com.practice.ecommerce.model.compositeId.ListId;
import com.practice.ecommerce.model.Product;
import com.practice.ecommerce.model.SavedProduct;
import com.practice.ecommerce.repository.SavedProductsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SavedProductsService {

    @Autowired
    private SavedProductsRepo savedProductsRepo;

    @Autowired
    private ProductService productService;

    public Product addToList(ListId listId, Integer productId) {
        Product product = productService.getProduct(productId);
        if (product == null) return null;
        Optional<SavedProduct> savedProduct = savedProductsRepo.findById(listId);

        if (savedProduct.isPresent()) {
            savedProduct.get().getProducts().add(product);
            savedProductsRepo.save(savedProduct.get());
        } else {
            SavedProduct newCustomer = new SavedProduct(listId.getIdentifier(), listId.getListType());
            newCustomer.getProducts().add(product);
            savedProductsRepo.save(newCustomer);
        }
        return product;
    }

    public List<Product> getListItems(ListType listType, String identifier) {
        Optional<SavedProduct> savedProduct = savedProductsRepo.findById(new ListId(identifier, listType));
        if (savedProduct.isEmpty()) {
            return null;
        }
        return savedProduct.get().getProducts();
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

    public boolean moveToCart(ListType listType, String identifier, Integer productId) {
        Product temp = addToList(new ListId(identifier, ListType.cart), productId);
        if (temp != null) {
            deleteListItem(listType, identifier, productId);
            return true;
        }
        return false;
    }
}
