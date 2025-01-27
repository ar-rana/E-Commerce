package com.practice.ecommerce.service;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class FirebaseTests {

    @Autowired
    private SearchService searchService;

    @Test
    public void testSearch() {
        List<String> tags = List.of("decore", "gift", "testNone");
        List<Integer> result = searchService.searchProduct(tags);

        assertFalse(result.isEmpty());
    }
}
