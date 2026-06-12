package com.example.tax_payment.persistence.repository;

import com.example.tax_payment.persistence.entity.InvoiceJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataInvoiceRepository
        extends JpaRepository<InvoiceJpaEntity, UUID> {

    List<InvoiceJpaEntity> findByTaxpayerTin(String taxpayerTin);

    Optional<InvoiceJpaEntity> findByInvoiceNumber(String invoiceNumber);
}