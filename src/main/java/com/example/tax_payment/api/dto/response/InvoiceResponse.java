package com.example.tax_payment.api.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record InvoiceResponse(
        UUID invoiceId,
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
) {}