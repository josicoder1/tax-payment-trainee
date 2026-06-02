package com.example.tax_payment.application.command;

import java.math.BigDecimal;

public record CreateInvoiceCommand(
        String taxpayerTin,
        String taxTypeCode,
        int taxYear,
        int taxMonth,
        BigDecimal principalAmount,
        BigDecimal interestAmount,
        BigDecimal penaltyAmount,
        String currency
) {
}