package com.example.tax_payment.api.dto.response;

import java.time.Instant;
import java.util.UUID;

public record InvoiceAuditResponse(
        UUID id,
        UUID invoiceId,
        String oldStatus,
        String newStatus,
        Instant changedAt
) {}