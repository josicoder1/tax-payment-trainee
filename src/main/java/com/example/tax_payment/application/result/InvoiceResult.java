package com.example.tax_payment.application.result;

import java.math.BigDecimal;
import java.util.UUID;

public record InvoiceResult(
        UUID invoiceId,
        String invoiceNumber,
        String taxpayerTin,
        String taxType,
        String taxPeriod,
        String status,

        BigDecimal principalAmount,
        BigDecimal interestAmount,
        BigDecimal penaltyAmount,

        BigDecimal paidPrincipal,
        BigDecimal paidInterest,
        BigDecimal paidPenalty,

        BigDecimal outstandingAmount,

        String currency
) {
}