package com.example.tax_payment.domain.exception;

public class InvoiceAlreadyPaidException
        extends DomainException {

    public InvoiceAlreadyPaidException() {

        super("invoice is already fully paid");
    }
}