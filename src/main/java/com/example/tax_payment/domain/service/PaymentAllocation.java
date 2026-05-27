package com.example.tax_payment.domain.service;

import com.example.tax_payment.domain.valueobject.Money;

import java.util.Objects;

public record PaymentAllocation(
        Money penaltyAllocated,
        Money interestAllocated,
        Money principalAllocated
) {
    public PaymentAllocation {
        Objects.requireNonNull(penaltyAllocated,
                "penaltyAllocated must not be null");

        Objects.requireNonNull(interestAllocated,
                "interestAllocated must not be null");

        Objects.requireNonNull(principalAllocated,
                "principalAllocated must not be null");
    }
}
