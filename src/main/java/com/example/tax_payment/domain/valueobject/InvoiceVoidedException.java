package com.example.tax_payment.domain.exception;

public class InvoiceVoidedException
        extends DomainException {

    public InvoiceVoidedException() {

        super("voided invoice cannot accept payments");
    }
}