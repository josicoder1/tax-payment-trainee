package com.example.tax_payment.application.command;

import java.math.BigDecimal;

public record PayInvoiceCommand(
        String idempotencyKey,
        String invoiceNumber,
        BigDecimal amount,
        String currency
        ) {
}
