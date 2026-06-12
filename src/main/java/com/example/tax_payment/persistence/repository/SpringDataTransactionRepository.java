package com.example.tax_payment.persistence.repository;

import com.example.tax_payment.persistence.entity.TransactionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SpringDataTransactionRepository
        extends JpaRepository<TransactionJpaEntity, UUID> {

    List<TransactionJpaEntity> findAllByOrderByCreatedAtDesc();

    List<TransactionJpaEntity> findByInvoiceIdOrderByCreatedAtDesc(UUID invoiceId);

    List<TransactionJpaEntity> findByPaymentIdOrderByCreatedAtDesc(UUID paymentId);

    List<TransactionJpaEntity> findByTypeOrderByCreatedAtDesc(String type);
}
