package com.practice.ecommerce.repository;

import com.practice.ecommerce.defaultModels.DefaultModels;
import com.practice.ecommerce.model.Enums.ListType;
import com.practice.ecommerce.model.Enums.ProductCategory;
import com.practice.ecommerce.model.Product;
import com.practice.ecommerce.model.SavedProduct;
import com.practice.ecommerce.model.compositeId.ListId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Rollback
public class SavedProductRepoTest {

    @Autowired
    private SavedProductsRepo savedProductsRepo;

    @Autowired
    private ProductRepository productRepository;

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
    private Product productRepoItem;
    private ListId wishlist = new ListId(DefaultModels.username, ListType.WISHLIST);
    private ListId cart = new ListId(DefaultModels.username, ListType.CART);

    @BeforeEach
    public void setUp() {
        productRepoItem = productRepository.save(product);
    }

    @Test
    public void testSave() {
        SavedProduct savedProduct = new SavedProduct(wishlist.getIdentifier(), wishlist.getListType());
        SavedProduct newSavedProduct = savedProductsRepo.save(savedProduct);

        assertEquals(savedProduct.getIdentifier(), newSavedProduct.getIdentifier());
        assertEquals(savedProduct.getListType(), newSavedProduct.getListType());
    }

    @Test
    public void testSave2() {
        SavedProduct savedProduct = new SavedProduct(cart.getIdentifier(), cart.getListType());
        SavedProduct newSavedProduct = savedProductsRepo.save(savedProduct);

        assertEquals(savedProduct.getIdentifier(), newSavedProduct.getIdentifier());
        assertEquals(savedProduct.getListType(), newSavedProduct.getListType());
    }

    @Test
    public void testFindById() {
        SavedProduct savedProduct = new SavedProduct(cart.getIdentifier(), cart.getListType());
        savedProduct.getProducts().add(productRepoItem);

        SavedProduct newSavedProduct = savedProductsRepo.save(savedProduct);

        assertNotNull(newSavedProduct);
        assertIterableEquals(savedProduct.getProducts(), newSavedProduct.getProducts());
        assertTrue(new ReflectionEquals(savedProduct, "productId").matches(newSavedProduct));
    }
}
