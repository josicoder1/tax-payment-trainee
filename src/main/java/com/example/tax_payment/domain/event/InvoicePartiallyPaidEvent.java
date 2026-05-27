package com.example.tax_payment.domain.event;

import java.time.Instant;
import java.util.UUID;

public record InvoicePartiallyPaidEvent(
        UUID invoiceId,
        Instant occurredAt
) implements DomainEvent {
}