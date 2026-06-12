package com.example.tax_payment.api.dto.request;


import java.math.BigDecimal;

public record PayInvoiceRequest(
        String idempotencyKey,
        String invoiceNumber,
        BigDecimal amount,
        String currency
) {
}
