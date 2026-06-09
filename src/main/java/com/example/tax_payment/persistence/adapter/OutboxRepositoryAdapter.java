package com.example.tax_payment.persistence.adapter;

import com.example.tax_payment.application.port.outbound.OutboxRepositoryPort;
import com.example.tax_payment.domain.event.DomainEvent;
import com.example.tax_payment.persistence.entity.OutboxEventJpaEntity;
import com.example.tax_payment.persistence.mapper.OutboxEventMapper;
import com.example.tax_payment.persistence.repository.SpringDataOutboxRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Adapter implementation for the outbox repository port.
 * Maps domain events to JPA entities and persists them to the outbox table.
 * Provides methods for querying unpublished events and marking them as published.
 */
@Component
public class OutboxRepositoryAdapter implements OutboxRepositoryPort {

    private final SpringDataOutboxRepository springDataOutboxRepository;
    private final OutboxEventMapper outboxEventMapper;

    public OutboxRepositoryAdapter(
            SpringDataOutboxRepository springDataOutboxRepository,
            OutboxEventMapper outboxEventMapper
    ) {
        this.springDataOutboxRepository = springDataOutboxRepository;
        this.outboxEventMapper = outboxEventMapper;
    }

    /**
     * Saves a domain event to the outbox table for eventual publishing.
     * The event is serialized to JSON and stored with published status set to false.
     *
     * @param event the domain event to save
     */
    @Override
    @Transactional
    public void save(DomainEvent event) {
        OutboxEventJpaEntity entity = outboxEventMapper.toEntity(event);
        springDataOutboxRepository.save(entity);
    }

    /**
     * Finds unpublished events from the outbox table ordered by creation time.
     * Used by the outbox poller to retrieve events that need to be published to Kafka.
     *
     * @param limit maximum number of events to retrieve
     * @return list of unpublished events in chronological order
     */
    @Transactional(readOnly = true)
    public List<OutboxEventJpaEntity> findUnpublishedEvents(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return springDataOutboxRepository.findByPublishedFalseOrderByCreatedAtAsc(pageable);
    }

    /**
     * Marks an event as published in the outbox table.
     * Sets the published flag to true and records the published timestamp.
     *
     * @param eventId the ID of the event to mark as published
     */
    @Transactional
    public void markAsPublished(UUID eventId) {
        OutboxEventJpaEntity entity = springDataOutboxRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Outbox event not found with ID: " + eventId));
        
        entity.setPublished(true);
        entity.setPublishedAt(Instant.now());
        
        springDataOutboxRepository.save(entity);
    }
}
