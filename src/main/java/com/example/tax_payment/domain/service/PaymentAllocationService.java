package com.example.tax_payment.domain.service;

import com.example.tax_payment.domain.exception.PaymentValidationException;
import com.example.tax_payment.domain.model.Invoice;
import com.example.tax_payment.domain.valueobject.Money;

import java.util.Objects;

public class PaymentAllocationService {

    public PaymentAllocation allocate(Invoice invoice, Money paymentAmount) {
        Objects.requireNonNull(invoice, "invoice must not be null");
        Objects.requireNonNull(paymentAmount, "paymentAmount must not be null");

        // Currency validation
        if (!paymentAmount.getCurrency().equals(invoice.getCurrency())) {
            throw new PaymentValidationException(
                    "Currency mismatch: payment in " + paymentAmount.getCurrency() +
                            ", invoice in " + invoice.getCurrency()
            );
        }

        Money zero = Money.zero(invoice.getCurrency());
        Money remaining = paymentAmount;

        Money penaltyAllocated = zero;
        Money interestAllocated = zero;
        Money principalAllocated = zero;

        // 1. Penalty first
        Money outstandingPenalty = invoice.getOutstandingPenalty();
        if (remaining.isGreaterThan(zero) && outstandingPenalty.isGreaterThan(zero)) {
            penaltyAllocated = remaining.isGreaterThan(outstandingPenalty) ? outstandingPenalty : remaining;
            remaining = remaining.subtract(penaltyAllocated);
        }

        // 2. Interest second
        Money outstandingInterest = invoice.getOutstandingInterest();
        if (remaining.isGreaterThan(zero) && outstandingInterest.isGreaterThan(zero)) {
            interestAllocated = remaining.isGreaterThan(outstandingInterest) ? outstandingInterest : remaining;
            remaining = remaining.subtract(interestAllocated);
        }

        // 3. Principal last
        Money outstandingPrincipal = invoice.getOutstandingPrincipal();
        if (remaining.isGreaterThan(zero) && outstandingPrincipal.isGreaterThan(zero)) {
            principalAllocated = remaining.isGreaterThan(outstandingPrincipal) ? outstandingPrincipal : remaining;
            remaining = remaining.subtract(principalAllocated);
        }

        if (remaining.isGreaterThan(zero)) {
            throw new PaymentValidationException(
                    "Payment amount exceeds total outstanding balance. Surplus: " + remaining
            );
        }

        return new PaymentAllocation(penaltyAllocated, interestAllocated, principalAllocated);
    }
}