package com.example.tax_payment.application.service;

import com.example.tax_payment.application.port.inbound.GenerateInvoicePdfUseCase;
import com.example.tax_payment.application.port.outbound.InvoicePdfPort;
import com.example.tax_payment.application.port.outbound.InvoiceRepositoryPort;
import com.example.tax_payment.domain.exception.InvoiceNotFoundException;
import com.example.tax_payment.domain.model.Invoice;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GenerateInvoicePdfService
        implements GenerateInvoicePdfUseCase {

    private final InvoiceRepositoryPort invoiceRepositoryPort;
    private final InvoicePdfPort invoicePdfPort;

    public GenerateInvoicePdfService(
            InvoiceRepositoryPort invoiceRepositoryPort,
            InvoicePdfPort invoicePdfPort) {

        this.invoiceRepositoryPort = invoiceRepositoryPort;
        this.invoicePdfPort = invoicePdfPort;
    }

    @Override
    public byte[] generateInvoicePdf(UUID invoiceId) {

        Invoice invoice = invoiceRepositoryPort.findById(invoiceId)
                .orElseThrow(() ->
                        new InvoiceNotFoundException(invoiceId));

        return invoicePdfPort.generate(invoice);
    }
}