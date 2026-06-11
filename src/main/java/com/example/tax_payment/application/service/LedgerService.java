package com.example.tax_payment.application.service;

import com.example.tax_payment.application.port.outbound.LedgerRepositoryPort;
import com.example.tax_payment.domain.model.LedgerEntry;
import com.example.tax_payment.domain.model.Payment;
import com.example.tax_payment.domain.model.Transaction;
import com.example.tax_payment.domain.valueobject.Money;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LedgerService {

    private final LedgerRepositoryPort ledgerRepository;

    public LedgerService(LedgerRepositoryPort ledgerRepository) {
        this.ledgerRepository = ledgerRepository;
    }

    public void recordPayment(
            Payment payment,
            Transaction transaction,
            Money amount
    ) {
        ledgerRepository.saveAll(List.of(
                LedgerEntry.debit(
                        transaction.getId(),
                        payment.getId(),
                        transaction.getInvoiceId(),
                        "CASH",
                        amount
                ),
                LedgerEntry.credit(
                        transaction.getId(),
                        payment.getId(),
                        transaction.getInvoiceId(),
                        "TAX_REVENUE",
                        amount
                )
        ));
    }
}