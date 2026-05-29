package com.example.tax_payment.domain.model;

import com.example.tax_payment.domain.event.DomainEvent;
import com.example.tax_payment.domain.event.InvoicePaidEvent;
import com.example.tax_payment.domain.event.InvoicePartiallyPaidEvent;
import com.example.tax_payment.domain.exception.InvalidPaymentAmountException;
import com.example.tax_payment.domain.exception.InvoiceAlreadyPaidException;
import com.example.tax_payment.domain.exception.InvoiceVoidedException;
import com.example.tax_payment.domain.exception.OverPaymentException;
import com.example.tax_payment.domain.valueobject.InvoiceStatus;
import com.example.tax_payment.domain.valueobject.Money;
import com.example.tax_payment.domain.valueobject.TaxPeriod;
import com.example.tax_payment.domain.valueobject.TaxTypeCode;
import com.example.tax_payment.domain.service.PaymentAllocation;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Invoice {

    private final UUID id;

    private final String taxpayerTin;

    private final TaxTypeCode taxType;

    private final TaxPeriod taxPeriod;

    private final Money principalAmount;

    private final Money interestAmount;

    private final Money penaltyAmount;

    private Money totalPaidPrincipal;

    private Money totalPaidInterest;

    private Money totalPaidPenalty;

    private InvoiceStatus status;

    private final List<DomainEvent> domainEvents = new ArrayList<>();

    public Invoice(
            UUID id,
            String taxpayerTin,
            TaxTypeCode taxType,
            TaxPeriod taxPeriod,
            Money principalAmount,
            Money interestAmount,
            Money penaltyAmount
    ) {

        this.id = Objects.requireNonNull(id);
        this.taxpayerTin = Objects.requireNonNull(taxpayerTin);
        this.taxType = Objects.requireNonNull(taxType);
        this.taxPeriod = Objects.requireNonNull(taxPeriod);

        this.principalAmount = Objects.requireNonNull(principalAmount);
        this.interestAmount = Objects.requireNonNull(interestAmount);
        this.penaltyAmount = Objects.requireNonNull(penaltyAmount);

        // Ensure all monetary values use same currency
        String currency = principalAmount.getCurrency();

        if (!currency.equals(interestAmount.getCurrency()) ||
                !currency.equals(penaltyAmount.getCurrency())) {

            throw new IllegalArgumentException(
                    "All invoice money values must use the same currency"
            );
        }

        // Initialize paid amounts with zero money in same currency
        this.totalPaidPrincipal = Money.zero(currency);
        this.totalPaidInterest = Money.zero(currency);
        this.totalPaidPenalty = Money.zero(currency);

        this.status = InvoiceStatus.OPEN;
    }

    public UUID getId() {
        return id;
    }

    public String getTaxpayerTin() {
        return taxpayerTin;
    }

    public TaxTypeCode getTaxType() {
        return taxType;
    }

    public TaxPeriod getTaxPeriod() {
        return taxPeriod;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    // Added for PaymentAllocationService
    public String getCurrency() {
        return principalAmount.getCurrency();
    }

    public Money getPrincipalAmount() {
        return principalAmount;
    }

    public Money getInterestAmount() {
        return interestAmount;
    }

    public Money getPenaltyAmount() {
        return penaltyAmount;
    }

    public Money getTotalPaidPrincipal() {
        return totalPaidPrincipal;
    }

    public Money getTotalPaidInterest() {
        return totalPaidInterest;
    }

    public Money getTotalPaidPenalty() {
        return totalPaidPenalty;
    }

    public Money getOutstandingPrincipal() {
        return principalAmount.subtract(totalPaidPrincipal);
    }

    public Money getOutstandingInterest() {
        return interestAmount.subtract(totalPaidInterest);
    }

    public Money getOutstandingPenalty() {
        return penaltyAmount.subtract(totalPaidPenalty);
    }

    public Money getTotalOutstanding() {
        return getOutstandingPenalty()
                .add(getOutstandingInterest())
                .add(getOutstandingPrincipal());
    }

    public boolean isFullyPaid() {
        return getTotalOutstanding().isZero();
    }

    public List<DomainEvent> pullDomainEvents() {

        List<DomainEvent> events = List.copyOf(domainEvents);

        domainEvents.clear();

        return events;
    }

    private void recalculateStatus() {

        if (isFullyPaid()) {

            status = InvoiceStatus.PAID;

            domainEvents.add(
                    new InvoicePaidEvent(
                            id,
                            Instant.now()
                    )
            );

            return;
        }

        status = InvoiceStatus.PARTIALLY_PAID;

        domainEvents.add(
                new InvoicePartiallyPaidEvent(
                        id,
                        Instant.now()
                )
        );
    }

    private void ensureInvoicePayable() {

        if (status == InvoiceStatus.VOIDED) {
            throw new InvoiceVoidedException();
        }

        if (status == InvoiceStatus.PAID) {
            throw new InvoiceAlreadyPaidException();
        }
    }

    private void ensurePositivePayment(Money amount) {

        if (!amount.isPositive()) {
            throw new InvalidPaymentAmountException();
        }
    }

    public void voidInvoice() {

        if (status == InvoiceStatus.PAID) {
            throw new IllegalStateException(
                    "paid invoice cannot be voided"
            );
        }

        status = InvoiceStatus.VOIDED;
    }

    public void applyPayment(PaymentAllocation allocation) {

        Objects.requireNonNull(allocation);

        ensureInvoicePayable();

        applyPenalty(allocation.penaltyAllocated());
        applyInterest(allocation.interestAllocated());
        applyPrincipal(allocation.principalAllocated());

        recalculateStatus();
    }

    private void applyPenalty(Money amount) {

        if (amount.isZero()) {
            return;
        }

        ensurePositivePayment(amount);

        Money outstanding = getOutstandingPenalty();

        if (amount.compareTo(outstanding) > 0) {
            throw new OverPaymentException("penalty");
        }

        totalPaidPenalty = totalPaidPenalty.add(amount);
    }

    private void applyInterest(Money amount) {

        if (amount.isZero()) {
            return;
        }

        ensurePositivePayment(amount);

        Money outstanding = getOutstandingInterest();

        if (amount.compareTo(outstanding) > 0) {
            throw new OverPaymentException("interest");
        }

        totalPaidInterest = totalPaidInterest.add(amount);
    }

    private void applyPrincipal(Money amount) {

        if (amount.isZero()) {
            return;
        }

        ensurePositivePayment(amount);

        Money outstanding = getOutstandingPrincipal();

        if (amount.compareTo(outstanding) > 0) {
            throw new OverPaymentException("principal");
        }

        totalPaidPrincipal = totalPaidPrincipal.add(amount);
    }


}