package com.example.tax_payment.infrastructure.messaging;

import com.example.tax_payment.application.port.outbound.EventPublisherPort;
import com.example.tax_payment.application.port.outbound.OutboxRepositoryPort;
import com.example.tax_payment.domain.event.DomainEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Outbox-based implementation of EventPublisherPort.
 * Saves domain events to the outbox table instead of publishing directly to Kafka.
 * The OutboxPoller will later read these events and publish them to Kafka.
 * 
 * This implements the Transactional Outbox Pattern to ensure reliable event delivery
 * without dual-write problems.
 * 
 * Marked as @Primary to be used as the default EventPublisherPort implementation.
 */
@Component
@Primary
public class OutboxEventPublisherAdapter implements EventPublisherPort {

    private static final Logger log = LoggerFactory.getLogger(OutboxEventPublisherAdapter.class);

    private final OutboxRepositoryPort outboxRepository;

    public OutboxEventPublisherAdapter(OutboxRepositoryPort outboxRepository) {
        this.outboxRepository = outboxRepository;
    }

    /**
     * Publishes domain events by saving them to the outbox table.
     * The events will be published to Kafka later by the OutboxPoller.
     * 
     * This method is called within the same transaction as the business operation,
     * ensuring atomicity between business state changes and event persistence.
     *
     * @param events the domain events to publish
     */
    @Override
    @Transactional
    public void publish(List<DomainEvent> events) {
        for (DomainEvent event : events) {
            try {
                outboxRepository.save(event);
                log.debug("Saved event to outbox: {}", event.getClass().getSimpleName());
            } catch (Exception e) {
                log.error("Failed to save event to outbox: type={}, error={}", 
                         event.getClass().getName(), 
                         e.getMessage(), 
                         e);
                throw e;
            }
        }
    }
}
