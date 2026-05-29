package com.example.tax_payment.application.result;

import java.math.BigDecimal;

public record AllocationResult(
        BigDecimal penaltyAllocated,
        BigDecimal interestAllocated,
        BigDecimal principalAllocated,
        String currency
) {
}