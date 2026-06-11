package com.example.tax_payment.application.port.outbound;

import com.example.tax_payment.domain.model.LedgerEntry;

import java.util.List;
import java.util.UUID;

public interface LedgerEntryRepositoryPort {

    void saveAll(List<LedgerEntry> entries);

    List<LedgerEntry> findAll();

    List<LedgerEntry> findByInvoiceId(UUID invoiceId);
}
