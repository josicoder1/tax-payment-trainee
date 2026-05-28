package com.example.tax_payment.domain.service;

import com.example.tax_payment.domain.valueobject.Money;

public record PaymentAllocation(
        Money penaltyAllocated,
        Money interestAllocated,
        Money principalAllocated
) {}