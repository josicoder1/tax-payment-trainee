package com.example.tax_payment.api.dto.request;


import java.math.BigDecimal;
import java.util.UUID;

public record PayInvoiceRequest(
        String idempotencyKey,
        UUID invoiceId,
        BigDecimal amount,
        String currency
) {
}
