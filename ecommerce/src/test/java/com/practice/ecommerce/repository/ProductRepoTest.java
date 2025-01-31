package com.practice.ecommerce.repository;

import java.util.List;
import java.util.Optional;

import com.practice.ecommerce.defaultModels.DefaultModels;
import com.practice.ecommerce.model.Enums.ProductCategory;
import com.practice.ecommerce.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Rollback
public class ProductRepoTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockRepository stockRepository;

    Product product = new Product(
            DefaultModels.productName1,
            DefaultModels.basicPrice1,
            DefaultModels.currentPrice1,
            DefaultModels.thumbnail,
            DefaultModels.stock,
            ProductCategory.homedecore,
            DefaultModels.stock - 10,
            "PNG"
    );

    private Product savedProduct;

    @BeforeEach
    public void setUp() {
        savedProduct = productRepository.save(product);
    }

    @Test
    public void testSaveProduct() {
        assertNotNull(savedProduct);
        assertEquals(product, savedProduct);
    }

    @Test
    public void testFindByCategory() {
        List<Product> product1 = productRepository.findByCategory(ProductCategory.aesthtic);
        List<Product> product2 = productRepository.findByCategory(ProductCategory.homedecore);

        assertNotNull(product2);
        assertTrue(product1.isEmpty());
        assertEquals(savedProduct.getCategory(), product2.get(0).getCategory());
    }

    @Test
    public void testFindCurrentPriceById() {
        Optional<Integer> currentPrice = productRepository.findCurrentPriceById(savedProduct.getProductId());

        assertTrue(currentPrice.isPresent());
        assertEquals(savedProduct.getCurrentPrice(), currentPrice.get());
    }

    @Test
    public void testFindAllByIdAndGetCurrentPrice() {
        List<Integer> prices = productRepository.findAllByIdAndGetCurrentPrice(List.of(savedProduct.getProductId()));

        assertFalse(prices.isEmpty());
        assertEquals(savedProduct.getCurrentPrice(), prices.get(0));
    }

    @Test
    public void testAlterCurrentPrice() {
        Integer alteredProducts = productRepository.alterCurrentPrice(500, savedProduct.getProductId());
        Optional<Product> newproduct = productRepository.findById(savedProduct.getProductId());

        assertEquals(alteredProducts, 1);
        assertTrue(newproduct.isPresent());
        assertEquals(500, newproduct.get().getCurrentPrice());
    }

    @Test
    public void testAdjustStockIncrement() {
        Integer originalStock = savedProduct.getStock();

        Integer incrementedRows = productRepository.adjustStock(1, savedProduct.getProductId());
        Optional<Product> product1 = productRepository.findById(savedProduct.getProductId());

        assertTrue(product1.isPresent());
        assertEquals(1, incrementedRows);
        assertEquals(originalStock+1, product1.get().getStock());
    }

        @Test
    public void testAdjustStockDecrement() {
        Integer originalStock = savedProduct.getStock();

        Integer decrementRows = productRepository.adjustStock(-1, savedProduct.getProductId());
        Optional<Product> product1 = productRepository.findById(savedProduct.getProductId());

        assertTrue(product1.isPresent());
        assertEquals(1, decrementRows);
        assertEquals(originalStock-1, product1.get().getStock());
    }

    @Test
    public void testSTOCKFindById() {
        Optional<Integer> currStock = productRepository.findAllByIdAndGetVirtualStock(savedProduct.getProductId());

        assertTrue(currStock.isPresent());
        assertEquals(savedProduct.getVirtualStock(), currStock.get());
    }

    @Test
    public void testAdjustVirtualStockIncrement() {
        int originalStock = savedProduct.getVirtualStock();

        Integer incrementedRows = productRepository.adjustVirtualStock(1, savedProduct.getProductId());
        Optional<Product> stock = productRepository.findById(savedProduct.getProductId());

        assertTrue(stock.isPresent());
        assertEquals(1, incrementedRows);
        assertEquals(originalStock+1, stock.get().getVirtualStock());
    }

    @Test
    public void testAdjustVirtualStockDecrement() {
        int originalStock = savedProduct.getVirtualStock();

        Integer incrementedRows = productRepository.adjustVirtualStock(-1, savedProduct.getProductId());
        Optional<Product> stock = productRepository.findById(savedProduct.getProductId());

        assertTrue(stock.isPresent());
        assertEquals(1, incrementedRows);
        assertEquals(originalStock-1, stock.get().getVirtualStock());
    }
}
