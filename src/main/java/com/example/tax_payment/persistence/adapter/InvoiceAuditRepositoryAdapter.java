package com.example.tax_payment.persistence.adapter;

import com.example.tax_payment.application.port.outbound.InvoiceAuditRepositoryPort;
import com.example.tax_payment.persistence.entity.InvoiceAuditJpaEntity;
import com.example.tax_payment.persistence.repository.InvoiceAuditJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class InvoiceAuditRepositoryAdapter
        implements InvoiceAuditRepositoryPort {

    private final InvoiceAuditJpaRepository repository;

    @Override
    public void save(InvoiceAuditJpaEntity audit) {
        repository.save(audit);
    }
}