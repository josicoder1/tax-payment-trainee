package com.example.tax_payment.persistence.repository;

import com.example.tax_payment.persistence.entity.PaymentJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataPaymentRepository
        extends JpaRepository<PaymentJpaEntity, UUID> {
}