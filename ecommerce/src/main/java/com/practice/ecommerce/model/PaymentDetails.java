package com.practice.ecommerce.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table
public class PaymentDetails {

    @Id
    @Column(columnDefinition = "TEXT")
    private String paymentId;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String paymentOrderId;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String paymentSignature;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String referenceNumber;

    @OneToMany(fetch = FetchType.EAGER)
    List<Order> orders;

    public PaymentDetails(String referenceNumber, String paymentSignature, String paymentOrderId, String paymentId) {
        this.referenceNumber = referenceNumber;
        this.paymentSignature = paymentSignature;
        this.paymentOrderId = paymentOrderId;
        this.paymentId = paymentId;
    }

    @Override
    public String toString() {
        return "PaymentDetails{" +
                "paymentId='" + paymentId + '\'' +
                ", paymentOrderId='" + paymentOrderId + '\'' +
                ", paymentSignature='" + paymentSignature + '\'' +
                ", referenceNumber='" + referenceNumber + '\'' +
                '}';
    }
}
