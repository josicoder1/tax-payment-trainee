package com.example.tax_payment.domain.service;

import com.example.tax_payment.domain.exception.PaymentValidationException;
import com.example.tax_payment.domain.model.Invoice;
import com.example.tax_payment.domain.valueobject.Money;

import java.util.Objects;

public class PaymentAllocationService {

    public PaymentAllocation allocate(
            Invoice invoice,
            Money paymentAmount
    ) {

        // Null validation
        Objects.requireNonNull(invoice,
                "invoice must not be null");

        Objects.requireNonNull(paymentAmount,
                "paymentAmount must not be null");

        Money remaining = paymentAmount;

        Money penaltyAllocated = Money.ZERO;
        Money interestAllocated = Money.ZERO;
        Money principalAllocated = Money.ZERO;

        // 1. Allocate to penalty first
        Money outstandingPenalty =
                invoice.getPenaltyAmount()
                        .subtract(invoice.getTotalPaidPenalty());

        if (remaining.isGreaterThan(Money.ZERO)
                && outstandingPenalty.isGreaterThan(Money.ZERO)) {

            penaltyAllocated =
                    remaining.isGreaterThan(outstandingPenalty)
                            ? outstandingPenalty
                            : remaining;

            remaining = remaining.subtract(penaltyAllocated);
        }

        // 2. Allocate to interest second
        Money outstandingInterest =
                invoice.getInterestAmount()
                        .subtract(invoice.getTotalPaidInterest());

        if (remaining.isGreaterThan(Money.ZERO)
                && outstandingInterest.isGreaterThan(Money.ZERO)) {

            interestAllocated =
                    remaining.isGreaterThan(outstandingInterest)
                            ? outstandingInterest
                            : remaining;

            remaining = remaining.subtract(interestAllocated);
        }

        // 3. Allocate to principal last
        Money outstandingPrincipal =
                invoice.getPrincipalAmount()
                        .subtract(invoice.getTotalPaidPrincipal());

        if (remaining.isGreaterThan(Money.ZERO)
                && outstandingPrincipal.isGreaterThan(Money.ZERO)) {

            principalAllocated =
                    remaining.isGreaterThan(outstandingPrincipal)
                            ? outstandingPrincipal
                            : remaining;

            remaining = remaining.subtract(principalAllocated);
        }

        // Prevent overpayment
        if (remaining.isGreaterThan(Money.ZERO)) {
            throw new PaymentValidationException(
                    "Payment amount exceeds total outstanding balance"
            );
        }

        return new PaymentAllocation(
                penaltyAllocated,
                interestAllocated,
                principalAllocated
        );
    }
}
