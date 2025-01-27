package com.practice.ecommerce.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.cloud.firestore.Blob;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.FirestoreException;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.SetOptions;

import com.practice.ecommerce.model.Enums.UserType;
import com.practice.ecommerce.model.Stock;
import com.practice.ecommerce.model.Product;
import com.practice.ecommerce.model.User;
import com.practice.ecommerce.repository.StockRepository;
import com.practice.ecommerce.repository.ProductRepository;
import com.practice.ecommerce.repository.UserRepository;
import io.netty.handler.codec.base64.Base64Encoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AdminService {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private Firestore firestore;

//    @Autowired
//    private Bucket storage;

    private static final String SEARCH_DB = "search";
    private static final String STORAGE_ADD = "images";
    private static final Logger logger = LoggerFactory.getLogger(AdminService.class);

    public Product addProduct(Product product, String tags) {
        Product savedProduct = productRepository.save(product);
        addToFirestore(parseList(tags), savedProduct.getProductId());
        return savedProduct;
    }

    public boolean changePrice(Integer price, Integer productId) {
        return productRepository.alterCurrentPrice(price, productId) > 0;
    }

    public User addAdmin(String user, UserType userType) {
        User admin = new User(user, userType);
        return userRepository.save(admin);
    }

    public String addToFirestore(List<String> tags, Integer productId) {
        for (String tag : tags) {
            var docRef = firestore.collection(SEARCH_DB).document(tag);

            try {
                docRef.update("productIds", FieldValue.arrayUnion(productId)).get(); // update doc if exists
                logger.info("Field updated (FIRESTORE): {}", tag);
            } catch (Exception e) {
                // create if not exist
                Map<String, List<Integer>> map = new HashMap<>();
                map.put("productIds", Arrays.asList(productId));
                docRef.set(map);
                logger.info("Field added (FIRESTORE): {}", tag);
            }
        }
        return "SUCCESS";
    }


    public String addToFirebaseStorage(Integer productId, MultipartFile file) {
//        String imageLocation = STORAGE_ADD + "/" + productId.toString() + "/" + UUID.randomUUID().toString();
//
//        BlobId blobId = BlobId.of(storage.getName(), imageLocation);
//        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
//
//        try {
//            storage.create(String.valueOf(blobInfo), file.getInputStream());
//        } catch (Exception ex) {
//            logger.error("Failed to add to STORAGE: {}", ex.getMessage());
//            return "FAILED";
//        }
        return "SUCCESS";
    }

    public List<String> parseList(String tagString) {
        return Stream.of(tagString.trim().split(","))
                .map(tag -> tag.trim().toLowerCase())
                .collect(Collectors.toList());
    }
}
