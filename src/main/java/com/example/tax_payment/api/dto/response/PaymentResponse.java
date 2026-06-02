package com.example.tax_payment.api.dto.response;

import java.time.Instant;
import java.util.UUID;

public record PaymentResponse(
        UUID paymentId,
        String referenceNumber,
        String status,
        String failureReason,
        Instant createdAt
) {}