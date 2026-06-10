package com.example.tax_payment.persistence.repository;

import com.example.tax_payment.persistence.entity.PaymentAuditJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataPaymentAuditRepository
        extends JpaRepository<PaymentAuditJpaEntity, UUID> {

}
