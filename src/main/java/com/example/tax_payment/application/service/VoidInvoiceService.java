package com.example.tax_payment.application.service;

import com.example.tax_payment.application.command.VoidInvoiceCommand;
import com.example.tax_payment.application.port.inbound.VoidInvoiceUseCase;
import com.example.tax_payment.application.port.outbound.InvoiceRepositoryPort;
import com.example.tax_payment.application.port.outbound.TransactionRepositoryPort;
import com.example.tax_payment.domain.model.Invoice;
import com.example.tax_payment.domain.model.Transaction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class VoidInvoiceService implements VoidInvoiceUseCase {

    private final InvoiceRepositoryPort repository;
    private final TransactionRepositoryPort transactionRepository;

    public VoidInvoiceService(
            InvoiceRepositoryPort repository,
            TransactionRepositoryPort transactionRepository
    ) {
        this.repository = repository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void voidInvoice(VoidInvoiceCommand command) {

        Invoice invoice = repository.findById(command.invoiceId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Invoice not found")
                );

        invoice.voidInvoice();

        repository.save(invoice);

        transactionRepository.save(
                Transaction.invoiceVoided(
                        invoice.getId(),
                        invoice.getTotalOutstanding()
                )
        );
    }
}