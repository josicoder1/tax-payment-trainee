package com.example.tax_payment.domain.exception;

import java.util.UUID;

public class InvoiceNotFoundException extends DomainException {

    public InvoiceNotFoundException(UUID id) {
        super("Invoice not found: " + id);
    }

    public InvoiceNotFoundException(String invoiceNumber) {
        super("Invoice not found: " + invoiceNumber);
    }
}