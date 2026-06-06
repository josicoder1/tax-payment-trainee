package com.example.tax_payment.persistence.repository;

import com.example.tax_payment.persistence.entity.PaymentJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpringDataPaymentRepository extends JpaRepository<PaymentJpaEntity, UUID> {

    Optional<PaymentJpaEntity> findFirstByReferenceNumber(String referenceNumber);

    List<PaymentJpaEntity> findByTaxpayerId(String taxpayerId);
}
