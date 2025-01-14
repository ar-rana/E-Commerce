package com.practice.ecommerce.model;

import com.practice.ecommerce.model.Enums.UserType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import org.springframework.data.annotation.Id;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    private String identifier;

    @Enumerated(EnumType.STRING)
    private UserType type;

    public int getId() {
        return id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public UserType getType() {
        return type;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", identifier='" + identifier + '\'' +
                ", type=" + type +
                '}';
    }
}
