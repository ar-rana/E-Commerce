package com.practice.ecommerce.repository;

import com.practice.ecommerce.defaultModels.DefaultModels;
import com.practice.ecommerce.model.Enums.ListType;
import com.practice.ecommerce.model.Product;
import com.practice.ecommerce.model.SavedProduct;
import com.practice.ecommerce.model.compositeId.ListId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@Rollback
public class SavedProductRepoTest {

    @Autowired
    private SavedProductsRepo savedProductsRepo;

    private Product product;
//    private Stock virtualStock;
    private ListId wishlist = new ListId(DefaultModels.username, ListType.WISHLIST);
    private ListId cart = new ListId(DefaultModels.username, ListType.CART);

    @BeforeEach
    public void setUp() {
        product = DefaultModels.product;
//        virtualStock = DefaultModels.virtualStock;

//        product.setVirtualStock(virtualStock);
    }

    @Test
    public void testSave() {
        SavedProduct savedProduct = new SavedProduct(wishlist.getIdentifier(), wishlist.getListType());
        SavedProduct newSavedProduct = savedProductsRepo.save(savedProduct);

        assertEquals(savedProduct.getIdentifier(), newSavedProduct.getIdentifier());
        assertEquals(savedProduct.getListType(), newSavedProduct.getListType());
        assertEquals(savedProduct.getProducts().size(), newSavedProduct.getProducts().size());
    }

    @Test
    public void testSave2() {
        SavedProduct savedProduct = new SavedProduct(cart.getIdentifier(), cart.getListType());
        SavedProduct newSavedProduct = savedProductsRepo.save(savedProduct);

        assertEquals(savedProduct.getIdentifier(), newSavedProduct.getIdentifier());
        assertEquals(savedProduct.getListType(), newSavedProduct.getListType());
        assertEquals(savedProduct.getProducts().size(), newSavedProduct.getProducts().size());
    }

    @Test
    public void testFindById() {
        SavedProduct savedProduct = new SavedProduct(cart.getIdentifier(), cart.getListType());
        savedProduct.getProducts().add(product);
        savedProduct.getProducts().add(DefaultModels.alternateProduct);

        SavedProduct newSavedProduct = savedProductsRepo.save(savedProduct);

        assertNotNull(newSavedProduct);
        assertEquals(savedProduct.getProducts().size(), newSavedProduct.getProducts().size());
    }
}
