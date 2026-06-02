package com.example.tax_payment.api.dto.request;

import java.math.BigDecimal;

public record PayInvoiceRequest(
        String taxpayerTin,
        String taxType,
        String taxPeriod,
        BigDecimal amount,
        String currency
) {}