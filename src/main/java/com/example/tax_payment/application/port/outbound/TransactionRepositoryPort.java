package com.example.tax_payment.application.port.outbound;

import com.example.tax_payment.domain.model.Transaction;

import java.util.List;
import java.util.UUID;

public interface TransactionRepositoryPort {

    void save(Transaction transaction);

    List<Transaction> findAll();

    List<Transaction> findByInvoiceId(UUID invoiceId);
}
