package com.example.tax_payment.application.result;

import java.math.BigDecimal;
import java.util.UUID;

public record InvoiceSummaryResult(
        UUID invoiceId,
        String invoiceNumber,
        String taxType,
        String taxPeriod,
        String status,
        BigDecimal outstandingAmount,
        String currency
) {
}
