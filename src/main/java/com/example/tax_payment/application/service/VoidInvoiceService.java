package com.example.tax_payment.application.service;

import com.example.tax_payment.application.command.VoidInvoiceCommand;
import com.example.tax_payment.application.port.inbound.VoidInvoiceUseCase;
import com.example.tax_payment.application.port.outbound.InvoiceRepositoryPort;
import com.example.tax_payment.domain.model.Invoice;

public class VoidInvoiceService implements VoidInvoiceUseCase {

    private final InvoiceRepositoryPort repository;

    public VoidInvoiceService(
            InvoiceRepositoryPort repository
    ) {
        this.repository = repository;
    }

    @Override
    public void voidInvoice(VoidInvoiceCommand command) {

        Invoice invoice = repository.findById(command.invoiceId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Invoice not found")
                );

        invoice.voidInvoice();

        repository.save(invoice);
    }
}