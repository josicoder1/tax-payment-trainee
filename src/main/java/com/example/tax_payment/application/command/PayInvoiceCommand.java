package com.example.tax_payment.application.command;

import java.math.BigDecimal;

public record PayInvoiceCommand(
        String taxpayerTin,
        String taxType,
        String taxPeriod,
        BigDecimal amount,
        String currency
) {
}