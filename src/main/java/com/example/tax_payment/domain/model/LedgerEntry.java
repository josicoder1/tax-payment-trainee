package com.example.tax_payment.domain.model;

import com.example.tax_payment.domain.valueobject.Money;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class LedgerEntry {

    private UUID id;
    private UUID transactionId;
    private UUID paymentId;
    private UUID invoiceId;

    private String account;   // CASH, TAX_REVENUE
    private String type;      // DEBIT / CREDIT

    private Money money;
    private Instant createdAt;

    public LedgerEntry(
            UUID id,
            UUID transactionId,
            UUID paymentId,
            UUID invoiceId,
            String account,
            String type,
            Money money,
            Instant createdAt
    ) {
        this.id = id;
        this.transactionId = transactionId;
        this.paymentId = paymentId;
        this.invoiceId = invoiceId;
        this.account = account;
        this.type = type;
        this.money = money;
        this.createdAt = createdAt;
    }

    public static LedgerEntry debit(UUID txId, UUID paymentId, UUID invoiceId, String account, Money money) {
        return new LedgerEntry(
                UUID.randomUUID(),
                txId,
                paymentId,
                invoiceId,
                account,
                "DEBIT",
                money,
                Instant.now()
        );
    }

    public static LedgerEntry credit(UUID txId, UUID paymentId, UUID invoiceId, String account, Money money) {
        return new LedgerEntry(
                UUID.randomUUID(),
                txId,
                paymentId,
                invoiceId,
                account,
                "CREDIT",
                money,
                Instant.now()
        );
    }
}