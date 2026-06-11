package com.example.tax_payment.application.result;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record LedgerEntryResult(
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
