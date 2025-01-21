package com.practice.ecommerce.repository;

import com.practice.ecommerce.model.Enums.ListType;
import com.practice.ecommerce.model.compositeId.ListId;
import com.practice.ecommerce.model.SavedProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SavedProductsRepo extends JpaRepository<SavedProduct, ListId> {

    SavedProduct findByIdentifierAndListType(String identifier, ListType listType);

    @Query("SELECT sp FROM SavedProduct sp WHERE sp.identifier = ?1 AND sp.listType = ?2")
    SavedProduct getAllSavedProduct(String identifier, ListType listType);
}
