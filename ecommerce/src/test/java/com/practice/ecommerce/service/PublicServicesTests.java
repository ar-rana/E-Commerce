package com.practice.ecommerce.service;

import java.util.List;
import java.util.Optional;

import com.practice.ecommerce.defaultModels.DefaultModels;
import com.practice.ecommerce.model.Product;
import com.practice.ecommerce.model.Stock;
import com.practice.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PublicServicesTests {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product1 = DefaultModels.product;
    private Product product2 = DefaultModels.alternateProduct;
    private Stock virtualStock = DefaultModels.virtualStock;

    @BeforeEach
    public void setUp() {
        product1.setVirtualStock(virtualStock);
        product2.setVirtualStock(virtualStock);

        product1.setProductId(DefaultModels.DEFAULT_ID);
        product2.setProductId(DefaultModels.DEFAULT_ID+1);
    }

    @Test
    public void testGetProduct() {
        when(productRepository.findById(DefaultModels.DEFAULT_ID)).thenReturn(Optional.ofNullable(product1));

        Product product = productService.getProduct(DefaultModels.DEFAULT_ID);

        verify(productRepository,times(1)).findById(DefaultModels.DEFAULT_ID);

        assertNotNull(product);
        assertEquals(product1, product);
    }

    @Test
    public void testGetPriceOnly() {
        when(productRepository.findCurrentPriceById(DefaultModels.DEFAULT_ID)).thenReturn(Optional.ofNullable(product1.getCurrentPrice()));

        Integer price = productService.getPriceOnly(DefaultModels.DEFAULT_ID);

        verify(productRepository, times(1)).findCurrentPriceById(DefaultModels.DEFAULT_ID);

        assertNotNull(price);
        assertEquals(product1.getCurrentPrice(), price);
    }

    @Test
    public void testGetTotalPrice() {
        Integer total = product1.getCurrentPrice() + product2.getCurrentPrice();
        List<Integer> productIds = List.of(DefaultModels.DEFAULT_ID, DefaultModels.DEFAULT_ID+1);
        when(productRepository.findAllByIdAndGetCurrentPrice(productIds))
                .thenReturn(List.of(product1.getCurrentPrice(), product2.getCurrentPrice())
        );

        Integer price = productService.getTotalPrice(productIds);

        verify(productRepository, times(1)).findAllByIdAndGetCurrentPrice(productIds);

        assertEquals(total, price);
    }

    @Test
    public void testGetProductByCategory() {
        when(productRepository.findByCategory(product1.getCategory())).thenReturn(List.of(product1));

        List<Product> products = productService.getProductByCategory(product1.getCategory());

        verify(productRepository, times(1)).findByCategory(product1.getCategory());

        assertEquals(List.of(product1), products);
    }
}
