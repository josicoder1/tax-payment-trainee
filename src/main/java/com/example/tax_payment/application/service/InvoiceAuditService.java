package com.example.tax_payment.application.service;

import com.example.tax_payment.persistence.entity.InvoiceAuditJpaEntity;
import com.example.tax_payment.persistence.repository.InvoiceAuditJpaRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class InvoiceAuditService {

    private final InvoiceAuditJpaRepository repository;

    public InvoiceAuditService(
            InvoiceAuditJpaRepository repository
    ) {
        this.repository = repository;
    }

    public void audit(
            UUID invoiceId,
            String oldStatus,
            String newStatus
    ) {

        InvoiceAuditJpaEntity audit =
                new InvoiceAuditJpaEntity();

        audit.setId(UUID.randomUUID());
        audit.setInvoiceId(invoiceId);
        audit.setOldStatus(oldStatus);
        audit.setNewStatus(newStatus);
        audit.setChangedAt(Instant.now());

        repository.save(audit);
    }
}