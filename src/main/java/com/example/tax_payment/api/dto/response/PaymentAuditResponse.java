package com.example.tax_payment.api.dto.response;

import java.time.Instant;
import java.util.UUID;

public record PaymentAuditResponse(
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
