package com.practice.ecommerce.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.core.type.TypeReference;
import com.practice.ecommerce.model.Enums.Keys;
import com.practice.ecommerce.model.Enums.ProductCategory;
import com.practice.ecommerce.model.Product;
import com.practice.ecommerce.repository.StockRepository;
import com.practice.ecommerce.repository.ProductRepository;
import com.practice.ecommerce.service.redis.RedisCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private SearchService searchService;

    @Autowired
    private RedisCacheService cache;

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    public Product getProduct(Integer id) { // cached
        String key = Keys.key(Keys.PRODUCT, id);
        Product item = cache.getCache(key, Product.class);
        if (item != null) {
            logger.info("Item from cache PRODUCT: {} - key: {}", item, key);
            return item;
        }
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            cache.setCache(key, product.get(), 10);
            return product.get();
        }
        return null;
    }

    public List<Product> getProducts(List<Integer> ids) {
        List<Product> products = new ArrayList<>();
        List<Integer> remainingIds = ids.stream().filter(id -> {
            String key = Keys.key(Keys.PRODUCT, id);
            Product item = cache.getCache(key, Product.class);
            if (item != null) {
                products.add(item);
                return false; // exclude
            }
            return true; // include
        }).toList();
        if (!remainingIds.isEmpty()) {
            List<Product> productFromDB = productRepository.findAllById(ids);
            products.addAll(productFromDB);
        }
        return products;
    }

    @Deprecated
    public Integer getVirtualStock(Integer id) {
        String key = Keys.key(Keys.STOCK, id);
        Integer item = cache.getCache(key, new TypeReference<Integer>() {});
        if (item != null) {
            logger.info("Item from cache VIRTUALSTOCK: {} - key: {}", item, key);
            return item;
        }
        Optional<Integer> stock = productRepository.findAllByIdAndGetVirtualStock(id);
        if (stock.isPresent()) {
            cache.setCache(key, stock.get(), 10);
            return stock.get();
        }
        return null;
    }

    public Integer getPriceOnly(Integer id) { // cached
        String key = Keys.key(Keys.PRICE, id.toString());
        Integer item = cache.getCache(key, new TypeReference<Integer>() {});
        if (item != null) {
            logger.info("Item from cache PRICE: {} - key: {}", item, key);
            return item;
        }
        Optional<Integer> price = productRepository.findCurrentPriceById(id);
        if (price.isPresent()) {
            cache.setCache(key, price.get(), 10);
            return price.get();
        }
        return null;
    }

    public Integer getTotalPrice(List<Integer> ids) {
        List<Integer> prices = productRepository.findAllByIdAndGetCurrentPrice(ids);
        Integer total = 0;
        for (Integer price: prices) {
            total += price;
        }
        return total;
    }

    public List<Product> getProductByCategory(ProductCategory category) { // cached
        String key = Keys.key(Keys.PRODUCT, category);
        List<Product> item = cache.getCache(key, new TypeReference<List<Product>>() {});
        if (item != null) {
            logger.info("Item from cache PRODUCT Category: {} - key: {}", item, key);
            return item;
        }
        List<Product> products = productRepository.findByCategory(category);
        cache.setCache(key, products, 10);
        return products;
    }

    public List<Product> getSearchProducts(String search) {
        List<Integer> productIds = searchService.searchProduct(Stream.of(search.trim().split(" "))
                                                                     .map(val -> val.trim().toLowerCase())
                                                                     .collect(Collectors.toList())
        );
        if (productIds.isEmpty()) return null;
        return getProducts(productIds);
    }

    public Product updateProductStock(Product product) {
        return productRepository.save(product);
    }
}
