package com.example.tax_payment.persistence.repository;

import com.example.tax_payment.persistence.entity.InvoiceAuditJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InvoiceAuditJpaRepository
        extends JpaRepository<InvoiceAuditJpaEntity, UUID> {
}