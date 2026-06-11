package com.example.tax_payment.application.service;

import com.example.tax_payment.application.port.inbound.ListLedgerEntriesUseCase;
import com.example.tax_payment.application.port.outbound.LedgerEntryRepositoryPort;
import com.example.tax_payment.application.result.LedgerEntryResult;
import com.example.tax_payment.domain.model.LedgerEntry;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ListLedgerEntriesService
        implements ListLedgerEntriesUseCase {

    private final LedgerEntryRepositoryPort repository;

    public ListLedgerEntriesService(
            LedgerEntryRepositoryPort repository
    ) {
        this.repository = repository;
    }

    @Override
    public List<LedgerEntryResult> listAll() {
        return repository.findAll().stream()
                .map(this::toResult)
                .toList();
    }

    @Override
    public List<LedgerEntryResult> listByInvoice(UUID invoiceId) {
        return repository.findByInvoiceId(invoiceId).stream()
                .map(this::toResult)
                .toList();
    }

    private LedgerEntryResult toResult(LedgerEntry entry) {
        return new LedgerEntryResult(
                entry.getId(),
                entry.getInvoiceId(),
                entry.getPaymentId(),
                entry.getTransactionId(),
                entry.getAccount().name(),
                entry.getEntrySide().name(),
                entry.getDescription(),
                entry.getMoney().getAmount(),
                entry.getMoney().getCurrency(),
                entry.getCreatedAt()
        );
    }
}
