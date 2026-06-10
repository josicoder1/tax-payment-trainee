package com.example.tax_payment.application.service;

import com.example.tax_payment.application.port.inbound.ListTransactionsUseCase;
import com.example.tax_payment.application.port.outbound.TransactionRepositoryPort;
import com.example.tax_payment.application.result.TransactionResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ListTransactionsService
        implements ListTransactionsUseCase {

    private final TransactionRepositoryPort repository;

    public ListTransactionsService(
            TransactionRepositoryPort repository
    ) {
        this.repository = repository;
    }

    @Override
    public List<TransactionResult> listAll() {
        return repository.findAll().stream()
                .map(tx -> new TransactionResult(
                        tx.getId(),
                        tx.getInvoiceId(),
                        tx.getPaymentId(),
                        tx.getType(),
                        tx.getDescription(),
                        tx.getMoney() != null ? tx.getMoney().getAmount() : null,
                        tx.getMoney() != null ? tx.getMoney().getCurrency() : null,
                        tx.getCreatedAt()
                ))
                .toList();
    }

    @Override
    public List<TransactionResult> listByInvoice(UUID invoiceId) {
        return repository.findByInvoiceId(invoiceId).stream()
                .map(tx -> new TransactionResult(
                        tx.getId(),
                        tx.getInvoiceId(),
                        tx.getPaymentId(),
                        tx.getType(),
                        tx.getDescription(),
                        tx.getMoney() != null ? tx.getMoney().getAmount() : null,
                        tx.getMoney() != null ? tx.getMoney().getCurrency() : null,
                        tx.getCreatedAt()
                ))
                .toList();
    }
}
