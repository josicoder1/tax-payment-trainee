package com.example.tax_payment.domain.event;

import com.example.tax_payment.domain.valueobject.Money;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class PaymentConfirmedEvent {

    private final UUID paymentId;
    private final String invoiceId;
    private final Money allocatedAmount;
    private final Instant confirmedAt;

    // Constructor with automatic timestamp
    public PaymentConfirmedEvent(
            UUID paymentId,
            String invoiceId,
            Money allocatedAmount
    ) {
        this(paymentId, invoiceId, allocatedAmount, Instant.now());
    }

    // Full constructor
    public PaymentConfirmedEvent(
            UUID paymentId,
            String invoiceId,
            Money allocatedAmount,
            Instant confirmedAt
    ) {

        this.paymentId = Objects.requireNonNull(
                paymentId,
                "paymentId must not be null"
        );

        this.invoiceId = Objects.requireNonNull(
                invoiceId,
                "invoiceId must not be null"
        );

        this.allocatedAmount = Objects.requireNonNull(
                allocatedAmount,
                "allocatedAmount must not be null"
        );

        this.confirmedAt = Objects.requireNonNull(
                confirmedAt,
                "confirmedAt must not be null"
        );
    }

    // Getters
    public UUID getPaymentId() {
        return paymentId;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public Money getAllocatedAmount() {
        return allocatedAmount;
    }

    public Instant getConfirmedAt() {
        return confirmedAt;
    }
}