package com.example.tax_payment.api.dto.request;

import java.math.BigDecimal;

public record CreateInvoiceRequest(
        String taxpayerTin,
        String taxTypeCode,
        int taxYear,
        int taxMonth,
        BigDecimal principalAmount,
        BigDecimal interestAmount,
        BigDecimal penaltyAmount,
        String currency
) {}