package com.example.tax_payment.domain.model;

import com.example.tax_payment.domain.valueobject.Money;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class Transaction {

    private final UUID id;
    private final UUID invoiceId;
    private final UUID paymentId;
    private final String type;
    private final String description;
    private final Money money;
    private final Instant createdAt;

    public Transaction(
            UUID id,
            UUID invoiceId,
            UUID paymentId,
            String type,
            String description,
            Money money,
            Instant createdAt
    ) {
        this.id = Objects.requireNonNull(id);
        this.invoiceId = invoiceId;
        this.paymentId = paymentId;
        this.type = Objects.requireNonNull(type);
        this.description = description;
        this.money = money;
        this.createdAt = Objects.requireNonNull(createdAt);
    }

    public UUID getId() {
        return id;
    }

    public UUID getInvoiceId() {
        return invoiceId;
    }

    public UUID getPaymentId() {
        return paymentId;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public Money getMoney() {
        return money;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public static Transaction invoiceCreated(UUID invoiceId, Money totalAmount) {
        return new Transaction(
                UUID.randomUUID(),
                invoiceId,
                null,
                "INVOICE_CREATED",
                "Invoice created",
                totalAmount,
                Instant.now()
        );
    }

    public static Transaction paymentReceived(UUID invoiceId, UUID paymentId, Money amount) {
        return new Transaction(
                UUID.randomUUID(),
                invoiceId,
                paymentId,
                "PAYMENT_RECEIVED",
                "Payment received and allocated",
                amount,
                Instant.now()
        );
    }

    public static Transaction invoiceVoided(UUID invoiceId, Money outstandingAmount) {
        return new Transaction(
                UUID.randomUUID(),
                invoiceId,
                null,
                "INVOICE_VOIDED",
                "Invoice voided",
                outstandingAmount,
                Instant.now()
        );
    }
}
