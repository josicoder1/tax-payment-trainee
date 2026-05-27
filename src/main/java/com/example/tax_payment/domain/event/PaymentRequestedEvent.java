package com.example.tax_payment.domain.event;


import com.example.tax_payment.domain.valueobject.Money;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record PaymentRequestedEvent(
        UUID paymentId,
        String invoiceId,
        Money amount,
        Instant requestedAt
) {

    // Constructor with automatic timestamp
    public PaymentRequestedEvent(
            UUID paymentId,
            String invoiceId,
            Money amount
    ) {
        this(paymentId, invoiceId, amount, Instant.now());
    }

    // Validation constructor
    public PaymentRequestedEvent {
        Objects.requireNonNull(paymentId,
                "paymentId must not be null");

        Objects.requireNonNull(invoiceId,
                "invoiceId must not be null");

        Objects.requireNonNull(amount,
                "amount must not be null");

        Objects.requireNonNull(requestedAt,
                "requestedAt must not be null");
    }
}