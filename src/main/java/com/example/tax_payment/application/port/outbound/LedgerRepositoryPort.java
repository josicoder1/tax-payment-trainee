package com.example.tax_payment.application.port.outbound;

import com.example.tax_payment.domain.model.LedgerEntry;

import java.util.List;

public interface LedgerRepositoryPort {
    void saveAll(List<LedgerEntry> entries);
    List<LedgerEntry> findAll();
}