package com.example.tax_payment.api.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record InvoiceSummaryResponse(
        UUID invoiceId,
        String taxType,
        String taxPeriod,
        String status,
        BigDecimal outstandingAmount,
        String currency
) {}