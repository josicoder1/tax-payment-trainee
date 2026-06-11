package com.example.tax_payment.domain.model;

import com.example.tax_payment.domain.service.PaymentAllocation;
import com.example.tax_payment.domain.valueobject.EntrySide;
import com.example.tax_payment.domain.valueobject.LedgerAccount;
import com.example.tax_payment.domain.valueobject.Money;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class LedgerEntry {

    private final UUID id;
    private final UUID invoiceId;
    private final UUID paymentId;
    private final UUID transactionId;
    private final LedgerAccount account;
    private final EntrySide entrySide;
    private final String description;
    private final Money money;
    private final Instant createdAt;

    public LedgerEntry(
            UUID id,
            UUID invoiceId,
            UUID paymentId,
            UUID transactionId,
            LedgerAccount account,
            EntrySide entrySide,
            String description,
            Money money,
            Instant createdAt
    ) {
        this.id = Objects.requireNonNull(id);
        this.invoiceId = Objects.requireNonNull(invoiceId);
        this.paymentId = paymentId;
        this.transactionId = transactionId;
        this.account = Objects.requireNonNull(account);
        this.entrySide = Objects.requireNonNull(entrySide);
        this.description = description;
        this.money = Objects.requireNonNull(money);
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

    public UUID getTransactionId() {
        return transactionId;
    }

    public LedgerAccount getAccount() {
        return account;
    }

    public EntrySide getEntrySide() {
        return entrySide;
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

    public static List<LedgerEntry> forInvoiceCreated(
            Invoice invoice,
            UUID transactionId
    ) {
        Instant createdAt = Instant.now();
        List<LedgerEntry> entries = new ArrayList<>();

        Money total = invoice.getPrincipalAmount()
                .add(invoice.getInterestAmount())
                .add(invoice.getPenaltyAmount());

        entries.add(entry(
                invoice.getId(),
                null,
                transactionId,
                LedgerAccount.RECEIVABLE,
                EntrySide.DEBIT,
                "Invoice receivable",
                total,
                createdAt
        ));

        addCreditIfPositive(
                entries,
                invoice.getId(),
                transactionId,
                LedgerAccount.PENALTY,
                "Penalty assessed",
                invoice.getPenaltyAmount(),
                createdAt
        );
        addCreditIfPositive(
                entries,
                invoice.getId(),
                transactionId,
                LedgerAccount.INTEREST,
                "Interest assessed",
                invoice.getInterestAmount(),
                createdAt
        );
        addCreditIfPositive(
                entries,
                invoice.getId(),
                transactionId,
                LedgerAccount.PRINCIPAL,
                "Principal assessed",
                invoice.getPrincipalAmount(),
                createdAt
        );

        return entries;
    }

    public static List<LedgerEntry> forPaymentReceived(
            UUID invoiceId,
            UUID paymentId,
            UUID transactionId,
            PaymentAllocation allocation,
            Money totalPayment
    ) {
        Instant createdAt = Instant.now();
        List<LedgerEntry> entries = new ArrayList<>();

        entries.add(entry(
                invoiceId,
                paymentId,
                transactionId,
                LedgerAccount.CASH,
                EntrySide.DEBIT,
                "Payment received",
                totalPayment,
                createdAt
        ));

        addReceivableCreditIfPositive(
                entries,
                invoiceId,
                paymentId,
                transactionId,
                "Penalty payment applied",
                allocation.penaltyAllocated(),
                createdAt
        );
        addReceivableCreditIfPositive(
                entries,
                invoiceId,
                paymentId,
                transactionId,
                "Interest payment applied",
                allocation.interestAllocated(),
                createdAt
        );
        addReceivableCreditIfPositive(
                entries,
                invoiceId,
                paymentId,
                transactionId,
                "Principal payment applied",
                allocation.principalAllocated(),
                createdAt
        );

        return entries;
    }

    public static List<LedgerEntry> forInvoiceVoided(
            Invoice invoice,
            UUID transactionId
    ) {
        Instant createdAt = Instant.now();
        List<LedgerEntry> entries = new ArrayList<>();

        Money outstanding = invoice.getTotalOutstanding();
        if (outstanding.isZero()) {
            return entries;
        }

        entries.add(entry(
                invoice.getId(),
                null,
                transactionId,
                LedgerAccount.RECEIVABLE,
                EntrySide.CREDIT,
                "Invoice voided — receivable reversed",
                outstanding,
                createdAt
        ));

        addDebitIfPositive(
                entries,
                invoice.getId(),
                transactionId,
                LedgerAccount.PENALTY,
                "Penalty reversed on void",
                invoice.getOutstandingPenalty(),
                createdAt
        );
        addDebitIfPositive(
                entries,
                invoice.getId(),
                transactionId,
                LedgerAccount.INTEREST,
                "Interest reversed on void",
                invoice.getOutstandingInterest(),
                createdAt
        );
        addDebitIfPositive(
                entries,
                invoice.getId(),
                transactionId,
                LedgerAccount.PRINCIPAL,
                "Principal reversed on void",
                invoice.getOutstandingPrincipal(),
                createdAt
        );

        return entries;
    }

    private static void addCreditIfPositive(
            List<LedgerEntry> entries,
            UUID invoiceId,
            UUID transactionId,
            LedgerAccount account,
            String description,
            Money amount,
            Instant createdAt
    ) {
        if (!amount.isZero()) {
            entries.add(entry(
                    invoiceId,
                    null,
                    transactionId,
                    account,
                    EntrySide.CREDIT,
                    description,
                    amount,
                    createdAt
            ));
        }
    }

    private static void addDebitIfPositive(
            List<LedgerEntry> entries,
            UUID invoiceId,
            UUID transactionId,
            LedgerAccount account,
            String description,
            Money amount,
            Instant createdAt
    ) {
        if (!amount.isZero()) {
            entries.add(entry(
                    invoiceId,
                    null,
                    transactionId,
                    account,
                    EntrySide.DEBIT,
                    description,
                    amount,
                    createdAt
            ));
        }
    }

    private static void addReceivableCreditIfPositive(
            List<LedgerEntry> entries,
            UUID invoiceId,
            UUID paymentId,
            UUID transactionId,
            String description,
            Money amount,
            Instant createdAt
    ) {
        if (!amount.isZero()) {
            entries.add(entry(
                    invoiceId,
                    paymentId,
                    transactionId,
                    LedgerAccount.RECEIVABLE,
                    EntrySide.CREDIT,
                    description,
                    amount,
                    createdAt
            ));
        }
    }

    private static LedgerEntry entry(
            UUID invoiceId,
            UUID paymentId,
            UUID transactionId,
            LedgerAccount account,
            EntrySide entrySide,
            String description,
            Money money,
            Instant createdAt
    ) {
        return new LedgerEntry(
                UUID.randomUUID(),
                invoiceId,
                paymentId,
                transactionId,
                account,
                entrySide,
                description,
                money,
                createdAt
        );
    }
}
