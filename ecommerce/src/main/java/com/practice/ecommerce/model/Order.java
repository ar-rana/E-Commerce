package com.practice.ecommerce.model;

import com.practice.ecommerce.model.Enums.DeliveryStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderId;

    @Column(nullable = false)
    public String userIdentifier;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    private String contact;
    @Column(nullable = false)
    private String referenceNumber;

    private String customer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

    public Order(String userIdentifier, DeliveryStatus status, Product product, String address, String contact, String name, String referenceNumber) {
        this.userIdentifier = userIdentifier;
        this.status = status;
        this.address = address;
        this.contact = contact;
        this.customer = name;
        this.referenceNumber = referenceNumber;
        this.product = product;
    }

    public Order() { }
}
