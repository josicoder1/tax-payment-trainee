package com.example.tax_payment.domain.event;

import com.example.tax_payment.domain.valueobject.Money;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record PaymentConfirmedEvent(
        UUID paymentId,
        String invoiceId,
        Money allocatedAmount,
        Instant confirmedAt
) {

    // Constructor with automatic timestamp
    public PaymentConfirmedEvent(
            UUID paymentId,
            String invoiceId,
            Money allocatedAmount
    ) {
        this(paymentId, invoiceId, allocatedAmount, Instant.now());
    }

    // Validation constructor
    public PaymentConfirmedEvent {
        Objects.requireNonNull(paymentId, "paymentId must not be null");
        Objects.requireNonNull(invoiceId, "invoiceId must not be null");
        Objects.requireNonNull(allocatedAmount, "allocatedAmount must not be null");
        Objects.requireNonNull(confirmedAt, "confirmedAt must not be null");
    }
}