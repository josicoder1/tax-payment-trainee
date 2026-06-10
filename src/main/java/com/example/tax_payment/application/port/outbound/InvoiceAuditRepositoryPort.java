package com.example.tax_payment.application.port.outbound;

import com.example.tax_payment.persistence.entity.InvoiceAuditJpaEntity;

public interface InvoiceAuditRepositoryPort {

    void save(InvoiceAuditJpaEntity audit);
}