package com.example.tax_payment.api.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record LedgerEntryResponse(
        UUID id,
        UUID invoiceId,
        UUID paymentId,
        UUID transactionId,
        String account,
        String entrySide,
        String description,
        BigDecimal amount,
        String currency,
        Instant createdAt
) {}
