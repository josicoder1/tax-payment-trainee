package com.example.tax_payment.infrastructure.messaging;

import com.example.tax_payment.application.port.outbound.EventPublisherPort;
import com.example.tax_payment.domain.event.DomainEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Kafka-based implementation of EventPublisherPort.
 * Publishes domain events to Apache Kafka topics using topic naming convention:
 * Event class name (e.g., InvoicePaidEvent) → topic name (e.g., "invoice-paid").
 * 
 * This adapter is used by the OutboxPoller to publish events from the outbox table
 * to Kafka. It is NOT used directly by application services (OutboxEventPublisherAdapter is).
 */
@Component
public class KafkaEventPublisherAdapter implements EventPublisherPort {

    private static final Logger log = LoggerFactory.getLogger(KafkaEventPublisherAdapter.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final EventSerializer eventSerializer;

    public KafkaEventPublisherAdapter(KafkaTemplate<String, String> kafkaTemplate,
                                       EventSerializer eventSerializer) {
        this.kafkaTemplate = kafkaTemplate;
        this.eventSerializer = eventSerializer;
    }

    /**
     * Publishes a list of domain events to Kafka topics.
     * Each event is serialized to JSON and sent to a topic derived from its class name.
     *
     * @param events the domain events to publish
     * @throws RuntimeException if publishing fails
     */
    @Override
    public void publish(List<DomainEvent> events) {
        for (DomainEvent event : events) {
            try {
                String topicName = deriveTopicName(event);
                String eventJson = eventSerializer.serialize(event);
                
                kafkaTemplate.send(topicName, eventJson);
                
                log.debug("Published event {} to topic {}", event.getClass().getSimpleName(), topicName);
            } catch (Exception e) {
                log.error("Failed to publish event: type={}, event={}, error={}", 
                         event.getClass().getName(), 
                         event.toString(), 
                         e.getMessage(), 
                         e);
                throw e;
            }
        }
    }

    /**
     * Derives Kafka topic name from event class name.
     * Conversion algorithm:
     * 1. Get simple class name (e.g., "InvoicePaidEvent")
     * 2. Remove "Event" suffix (e.g., "InvoicePaid")
     * 3. Convert to kebab-case (e.g., "invoice-paid")
     *
     * @param event the domain event
     * @return the Kafka topic name
     */
    private String deriveTopicName(DomainEvent event) {
        String className = event.getClass().getSimpleName();
        
        // Remove "Event" suffix if present
        if (className.endsWith("Event")) {
            className = className.substring(0, className.length() - 5);
        }
        
        // Convert to kebab-case
        return toKebabCase(className);
    }

    /**
     * Converts a PascalCase string to kebab-case.
     * Example: "InvoicePaid" → "invoice-paid"
     *
     * @param str the PascalCase string
     * @return the kebab-case string
     */
    private String toKebabCase(String str) {
        return str.replaceAll("([a-z])([A-Z])", "$1-$2").toLowerCase();
    }
}