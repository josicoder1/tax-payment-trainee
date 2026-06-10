package com.example.tax_payment.application.result;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record TransactionResult(
        UUID id,
        UUID invoiceId,
        UUID paymentId,
        String type,
        String description,
        BigDecimal amount,
        String currency,
        Instant createdAt
) {}
