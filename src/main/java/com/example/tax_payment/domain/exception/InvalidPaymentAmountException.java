package com.example.tax_payment.domain.exception;

public class InvalidPaymentAmountException
        extends DomainException {

    public InvalidPaymentAmountException() {

        super("payment amount must be positive");
    }
}