package com.example.tax_payment.persistence.adapter;

import com.example.tax_payment.application.port.outbound.EventPublisherPort;
import com.example.tax_payment.domain.event.DomainEvent;
import java.util.List;

public class SimpleEventPublisherAdapter
        implements EventPublisherPort {

    @Override
    public void publish(
            List<DomainEvent> events
    ) {

        events.forEach(System.out::println);
    }
}