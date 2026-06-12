package com.example.tax_payment.application.result;

import java.time.Instant;
import java.util.UUID;

public record PaymentResult(
        UUID paymentId,
        String referenceNumber,
        String status,
        String failureReason,
        String message,
        Instant createdAt,
        AllocationResult allocation
) {
}