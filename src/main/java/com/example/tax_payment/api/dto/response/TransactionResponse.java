package com.example.tax_payment.api.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record TransactionResponse(
        UUID id,
        UUID invoiceId,
        UUID paymentId,
        String type,
        String description,
        BigDecimal amount,
        String currency,
        Instant createdAt
) {}
