package com.example.tax_payment.application.result;

import java.time.Instant;
import java.util.UUID;

public record PaymentAuditResult(
        UUID id,
        UUID paymentId,
        String eventType,
        String oldStatus,
        String newStatus,
        String reason,
        String idempotencyKey,
        String payload,
        Instant createdAt
) {
}
