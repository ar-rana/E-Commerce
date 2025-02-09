package com.practice.ecommerce.repository;

import java.util.Optional;

import com.practice.ecommerce.model.PaymentDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentsRepository extends JpaRepository<PaymentDetails, String> {
    Optional<PaymentDetails> findByReferenceNumber(String referenceNumber);
}
