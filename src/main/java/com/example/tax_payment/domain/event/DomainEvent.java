package com.example.tax_payment.domain.event;

import java.time.Instant;

public interface DomainEvent {

    Instant occurredAt();
}
