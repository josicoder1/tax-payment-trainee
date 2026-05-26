package com.example.tax_payment.domain.exception;


import com.example.tax_payment.domain.exception.DomainException;

public class InvoiceVoidedException
        extends DomainException {

    public InvoiceVoidedException() {

        super("voided invoice cannot accept payments");
    }
}
