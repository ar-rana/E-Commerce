package com.practice.ecommerce.model;

import java.util.ArrayList;
import java.util.List;

import com.practice.ecommerce.model.Enums.ListType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

@Entity
@IdClass(ListId.class)
public class SavedProduct {

    @Id
    private String identifier;
    @Id
    @Enumerated(EnumType.STRING)
    private ListType listType;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Product> products = new ArrayList<>();

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public ListType getListType() {
        return listType;
    }

    public void setListType(ListType listType) {
        this.listType = listType;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public SavedProduct(String identifier, ListType listType) {
        this.identifier = identifier;
        this.listType = listType;
    }

    public SavedProduct() { }

    @Override
    public String toString() {
        return "SavedProduct{" +
                "identifier='" + identifier + '\'' +
                ", listType=" + listType +
                ", products=" + products +
                '}';
    }
}
