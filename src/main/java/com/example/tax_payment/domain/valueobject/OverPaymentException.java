package com.example.tax_payment.domain.exception;

public class OverPaymentException extends DomainException {

    public OverPaymentException(String category) {

        super("payment exceeds outstanding " + category + " amount");
    }
}