package com.example.tax_payment.api.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateInvoiceRequest(
        @NotBlank String taxpayerTin,
        @NotBlank String taxTypeCode,
        @NotNull Integer taxYear,
        @NotNull Integer taxMonth,
        @NotNull BigDecimal principalAmount,
        @NotNull BigDecimal interestAmount,
        @NotNull BigDecimal penaltyAmount,
        @NotBlank String currency
) {}