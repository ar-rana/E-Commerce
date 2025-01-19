package com.practice.ecommerce.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import com.practice.ecommerce.model.Enums.ListType;

@Entity
@Table(name = "cart")
public class Cart {

    @Id
    private String userIdentifier;
    // join two rowws id and identifier
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Product> products = new ArrayList<>();

    public Cart(String userIdentifier) {
        this.userIdentifier = userIdentifier;
    }

    public String getUserIdentifier() {
        return userIdentifier;
    }

    public void setUserIdentifier(String userIdentifier) {
        this.userIdentifier = userIdentifier;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Cart() { }
}
