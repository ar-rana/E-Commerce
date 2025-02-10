package com.practice.ecommerce.model;

import java.util.ArrayList;
import java.util.List;

import com.practice.ecommerce.model.Enums.ListType;
import com.practice.ecommerce.model.compositeId.ListId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@IdClass(ListId.class)
@Getter
@Setter
public class SavedProduct {

    @Id
    private String identifier;
    @Id
    @Enumerated(EnumType.STRING)
    private ListType listType;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Product> products = new ArrayList<>();

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
