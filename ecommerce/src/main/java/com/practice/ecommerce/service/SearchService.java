package com.practice.ecommerce.service;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.practice.ecommerce.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchService {

    @Autowired
    private Firestore firestore;

    private static final String SEARCH_DB = "search";
    private static final Logger logger = LoggerFactory.getLogger(SearchService.class);

    public List<Integer> searchProduct(List<String> tags) {
        List<Integer> productIds = new ArrayList<>();
        for (String tag: tags) {
            try {
                DocumentSnapshot snapshot = firestore.collection(SEARCH_DB).document(tag).get().get();
                if (snapshot.exists()) {
                    List<Long> list = (List<Long>) snapshot.get("productIds");
//                    logger.error("FIRESTORE productIds: {} for tag: {} {}", snapshot.get("productIds"), tag, snapshot.get("productIds").getClass());
                    list.forEach((id) -> productIds.add(id.intValue()));
                }
            } catch (InterruptedException e) {
                logger.error("FIRESTORE query interrupted: {}", e.getMessage());
            } catch (Exception ex) {
                logger.error("Failed to fetch FIRESTORE search: {}", ex.getMessage());
            }
        }
        return productIds;
    }
}
