package com.practice.ecommerce.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.core.type.TypeReference;
import com.practice.ecommerce.model.Enums.EmailMessages;
import com.practice.ecommerce.model.Enums.Keys;
import com.practice.ecommerce.model.Enums.ProductCategory;
import com.practice.ecommerce.model.Image;
import com.practice.ecommerce.model.Product;
import com.practice.ecommerce.model.Review;
import com.practice.ecommerce.repository.ImagesRepository;
import com.practice.ecommerce.repository.ProductRepository;
import com.practice.ecommerce.service.redis.Publisher;
import com.practice.ecommerce.service.redis.RedisCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ImagesRepository imagesRepository;

    @Autowired
    private SearchService searchService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private RedisCacheService cache;

    @Autowired
    private Publisher publisher;

    @Value("${app.admin.mail}")
    private String admin;

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    public Product getProduct(Integer id) { // cached
        String key = Keys.key(Keys.PRODUCT, id);
        Product item = cache.getCache(key, Product.class);
        if (item != null) {
            logger.info("Item from cache PRODUCT with key: {}", key);
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
        if (item != null && !item.isEmpty()) {
            logger.info("Item from cache PRODUCT Category: {} - key: {}", category, key);
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

    public List<Integer> getImagesForProduct(Integer productId) {
        return imagesRepository.findByProductIdAndGetImageId(productId);
    }

    public Image getImagesById(Integer imageId) {
        logger.info("Image Fetched ID: {}", imageId);
        return imagesRepository.findById(imageId).orElse(null);
    }

    public List<Product> getRandomProduct() {
        Integer limit = 10;
        List<Product> items = cache.getMatchers("product/*", Product.class, limit);
        if (items != null) {
            logger.info("Random Products From Cache: {} items", limit);
            return items;
        }
        List<Product> products = productRepository.findRandomProducts(limit);
        products.forEach(product -> {
            cache.setCache(Keys.key(Keys.PRODUCT, product.getProductId()), product, 6);
        });
        return products;
    }

    public List<Review> getReviews(Integer productId) {
        List<Review> reviews = reviewService.getReviews(productId);
        if (reviews.isEmpty()) return null;
        return reviews;
    }

    public List<Review> getNewReviews(Integer productId, List<String> reviewIds) {
        List<Review> reviews = reviewService.getNewReviews(productId, reviewIds);
        if (reviews.isEmpty()) return null;
        return reviews;
    }

    public void adjustStock(Integer amt, Integer productId) {
        String key = Keys.key(Keys.PRODUCT, productId);
        Product item = cache.getCache(key, Product.class);
        if (item != null) {
            item.setStock(item.getStock() + amt);
            item.setVirtualStock(item.getVirtualStock() + amt);
            Product product = productRepository.save(item);
            if (product.getVirtualStock() == 0) {
                Map<String, String> map = new HashMap<>();
                map.put("type", EmailMessages.productStockOver.name());
                map.put("to", admin);
                map.put("productId", item.getProductId().toString());
                publisher.publishToStream(map);
            }
            cache.setCache(key, item, 100);
            return;
        }
        productRepository.adjustStock(amt, productId);
        adjustVirtualStock(amt, productId);
    }

    public void adjustVirtualStock(Integer amt, Integer productId) {
        productRepository.adjustVirtualStock(amt, productId);
    }

    public Product updateProductStock(Product product) {
        return productRepository.save(product);
    }
}
