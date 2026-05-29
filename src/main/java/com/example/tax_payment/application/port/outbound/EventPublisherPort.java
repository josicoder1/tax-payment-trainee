package com.example.tax_payment.application.port.outbound;

import com.example.tax_payment.domain.event.DomainEvent;

import java.util.List;

public interface EventPublisherPort {

    void publish(List<DomainEvent> events);
}