package com.example.tax_payment.persistence.repository;

import com.example.tax_payment.persistence.entity.InvoiceStatusJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataInvoiceStatusRepository
        extends JpaRepository<InvoiceStatusJpaEntity, Long> {

    Optional<InvoiceStatusJpaEntity> findByCode(String code);
}