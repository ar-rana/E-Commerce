package com.practice.ecommerce.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.ecommerce.model.Enums.Keys;
import com.practice.ecommerce.model.Enums.ListType;
import com.practice.ecommerce.model.compositeId.ListId;
import com.practice.ecommerce.model.Product;
import com.practice.ecommerce.model.SavedProduct;
import com.practice.ecommerce.repository.SavedProductsRepo;
import com.practice.ecommerce.service.redis.RedisCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SavedProductsService {

    @Autowired
    private SavedProductsRepo savedProductsRepo;

    @Autowired
    private ProductService productService;

    @Autowired
    private RedisCacheService cache;

    private static final Logger logger = LoggerFactory.getLogger(SavedProductsService.class);

    public Product addToList(ListId listId, Integer productId) {
        String key = Keys.key(Keys.valueOf(listId.getListType().name()), listId.getIdentifier());
        Product product = productService.getProduct(productId);
        if (product == null) return null;
        List<Product> items = cache.getCache(key, new TypeReference<List<Product>>() {});
        if (items != null) {
            ObjectMapper mapper = new ObjectMapper();
            items.add(product);
            Map<String, String> map = new HashMap<>();
            try {
                map.put("listId", mapper.writeValueAsString(listId));
            } catch (JsonProcessingException ex) {
                logger.error("FAILED TO PARSE ListId: {}", ex.getMessage());
                return null;
            }
            cache.setCache(key, items, 15);
            cache.writeThroughCache(key, map);
            return product;
        }
        Optional<SavedProduct> savedProduct = savedProductsRepo.findById(listId);

        if (savedProduct.isPresent()) {
            savedProduct.get().getProducts().add(product);
            savedProductsRepo.save(savedProduct.get());
            cache.setCache(key, savedProduct.get().getProducts(), 15);
        } else {
            SavedProduct newCustomer = new SavedProduct(listId.getIdentifier(), listId.getListType());
            newCustomer.getProducts().add(product);
            savedProductsRepo.save(newCustomer);
            cache.setCache(key, newCustomer.getProducts(), 15);
        }
        return product;
    }

    public List<Product> getListItems(ListType listType, String identifier) {
        String key = Keys.key(Keys.valueOf(listType.name()), identifier);
        List<Product> item = cache.getCache(key, new TypeReference<List<Product>>() {});
        if (item != null) {
            logger.info("Item from cache List<PRODUCT>: key: {}", key);
            return item;
        }
        Optional<SavedProduct> savedProduct = savedProductsRepo.findById(new ListId(identifier, listType));
        if (savedProduct.isPresent()) {
            cache.setCache(key, savedProduct.get().getProducts(), 15);
            return savedProduct.get().getProducts();
        }
        return null;
    }

    public boolean deleteListItem(ListType listType, String identifier, Integer productId) {
        String key = Keys.key(Keys.valueOf(listType.name()), identifier);
        List<Product> products = getListItems(listType, identifier);
        if (products == null || products.isEmpty()) {
            return false;
        }
        boolean found = false;
        SavedProduct savedProduct = new SavedProduct(identifier, listType);
        for (Product pr : products) {
            if (pr.getProductId() == productId && !found) {
                found = true;
                continue;
            }
            savedProduct.getProducts().add(pr);
        }
        savedProductsRepo.save(savedProduct);
        cache.setCache(key, savedProduct.getProducts(), 10);
        return true;
    }

    public boolean moveToCart(ListType listType, ListType from, String identifier, Integer productId) {
        String key = Keys.key(Keys.valueOf(listType.name()), identifier);
        Product temp = addToList(new ListId(identifier, listType), productId);
        if (temp != null) {
            deleteListItem(from, identifier, productId);
            cache.deleteCache(key); // write through cache
            return true;
        }
        return false;
    }

    public boolean clearCart(String identifier) {
        savedProductsRepo.deleteById(new ListId(identifier, ListType.CART));
        return true;
    }
}
