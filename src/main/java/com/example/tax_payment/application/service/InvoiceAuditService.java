package com.example.tax_payment.application.service;

import com.example.tax_payment.persistence.entity.InvoiceAuditJpaEntity;
import com.example.tax_payment.persistence.repository.InvoiceAuditJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class InvoiceAuditService {

    private final InvoiceAuditJpaRepository repository;

    public InvoiceAuditService(InvoiceAuditJpaRepository repository) {
        this.repository = repository;
    }


    @Transactional
    public void audit(UUID invoiceId, String oldStatus, String newStatus) {
        InvoiceAuditJpaEntity audit = new InvoiceAuditJpaEntity();

        audit.setId(UUID.randomUUID());
        audit.setInvoiceId(invoiceId);
        audit.setOldStatus(oldStatus);
        audit.setNewStatus(newStatus);
        audit.setChangedAt(Instant.now());

        repository.save(audit);
    }

    // ADD THIS: Method to pull the history out
    @Transactional(readOnly = true)
    public List<InvoiceAuditJpaEntity> getHistory(UUID invoiceId) {
        return repository.findByInvoiceIdOrderByChangedAtDesc(invoiceId);
    }
}