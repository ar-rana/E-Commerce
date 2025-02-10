package com.practice.ecommerce.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.practice.ecommerce.model.Enums.UserType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @Column(unique = true, nullable = false)
    public String identifier;

    @Enumerated(EnumType.STRING)
    private UserType type;

    @OneToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    public List<Order> orders = new ArrayList<>();

    public User(String identifier, UserType type) {
        this.identifier = identifier;
        this.type = type;
    }

    public User() { }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", identifier='" + identifier + '\'' +
                ", type=" + type +
                ", orders=" + orders +
                '}';
    }
}
