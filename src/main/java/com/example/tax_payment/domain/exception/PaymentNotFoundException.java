package com.example.tax_payment.domain.exception;

import java.util.UUID;

public class PaymentNotFoundException extends DomainException {

    public PaymentNotFoundException(UUID id) {
        super("Payment not found: " + id);
    }
}