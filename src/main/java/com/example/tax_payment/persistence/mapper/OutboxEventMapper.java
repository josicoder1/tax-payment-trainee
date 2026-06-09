package com.example.tax_payment.persistence.mapper;

import com.example.tax_payment.domain.event.DomainEvent;
import com.example.tax_payment.infrastructure.messaging.EventSerializer;
import com.example.tax_payment.persistence.entity.OutboxEventJpaEntity;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

/**
 * Mapper for converting between domain events and outbox JPA entities.
 * Handles the serialization of domain events to JSON for storage in the outbox table.
 */
@Component
public class OutboxEventMapper {

    private final EventSerializer eventSerializer;

    public OutboxEventMapper(EventSerializer eventSerializer) {
        this.eventSerializer = eventSerializer;
    }

    /**
     * Converts a domain event to an outbox JPA entity.
     * The event is serialized to JSON and stored with metadata.
     *
     * @param event the domain event to convert
     * @return the outbox JPA entity ready for persistence
     */
    public OutboxEventJpaEntity toEntity(DomainEvent event) {
        OutboxEventJpaEntity entity = new OutboxEventJpaEntity();
        entity.setId(UUID.randomUUID());
        entity.setEventType(event.getClass().getName());
        entity.setEventData(eventSerializer.serialize(event));
        entity.setOccurredAt(event.occurredAt());
        entity.setPublished(false);
        entity.setPublishedAt(null);
        entity.setCreatedAt(Instant.now());
        return entity;
    }

    /**
     * Converts an outbox JPA entity back to a domain event.
     * Deserializes the JSON event data back to the appropriate domain event type.
     *
     * @param entity the outbox JPA entity to convert
     * @return the deserialized domain event
     */
    public DomainEvent toDomainEvent(OutboxEventJpaEntity entity) {
        return eventSerializer.deserialize(entity.getEventData());
    }
}
