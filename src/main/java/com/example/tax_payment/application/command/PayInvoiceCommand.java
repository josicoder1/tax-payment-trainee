package com.example.tax_payment.application.command;

import java.math.BigDecimal;
import java.util.UUID;

public record PayInvoiceCommand(
        UUID invoiceId,
        BigDecimal amount,
        String currency
) {
}
