package com.example.tax_payment.infrastructure.messaging;

import com.example.tax_payment.domain.event.DomainEvent;
import com.example.tax_payment.persistence.adapter.OutboxRepositoryAdapter;
import com.example.tax_payment.persistence.entity.OutboxEventJpaEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Background poller that retrieves unpublished events from the outbox table
 * and publishes them to Kafka. Implements the outbox pattern for reliable
 * event delivery even when Kafka is temporarily unavailable.
 * 
 * Enabled only in dev/prod profiles via ConditionalOnProperty.
 */
@Component
@ConditionalOnProperty(
    name = "outbox.poller.enabled",
    havingValue = "true",
    matchIfMissing = false
)
public class OutboxPoller {

    private static final Logger log = LoggerFactory.getLogger(OutboxPoller.class);

    private final OutboxRepositoryAdapter outboxRepositoryAdapter;
    private final KafkaEventPublisherAdapter kafkaEventPublisher;
    private final EventSerializer eventSerializer;
    
    @Value("${outbox.poller.batch-size:100}")
    private int batchSize;

    public OutboxPoller(
            OutboxRepositoryAdapter outboxRepositoryAdapter,
            KafkaEventPublisherAdapter kafkaEventPublisher,
            EventSerializer eventSerializer
    ) {
        this.outboxRepositoryAdapter = outboxRepositoryAdapter;
        this.kafkaEventPublisher = kafkaEventPublisher;
        this.eventSerializer = eventSerializer;
    }

    /**
     * Scheduled task that polls the outbox table for unpublished events
     * and publishes them to Kafka. Runs with a fixed delay between executions
     * as configured in application.properties.
     * 
     * For each event:
     * - Deserializes the event from JSON
     * - Publishes to Kafka via KafkaEventPublisherAdapter
     * - Marks as published on success
     * - Logs error and continues on failure (will retry in next cycle)
     */
    @Scheduled(fixedDelayString = "${outbox.poller.fixed-delay-ms:5000}")
    @Transactional
    public void pollAndPublish() {
        List<OutboxEventJpaEntity> unpublishedEvents = 
            outboxRepositoryAdapter.findUnpublishedEvents(batchSize);
        
        if (unpublishedEvents.isEmpty()) {
            log.trace("No unpublished events found in outbox");
            return;
        }
        
        log.debug("Found {} unpublished events to process", unpublishedEvents.size());
        
        for (OutboxEventJpaEntity eventEntity : unpublishedEvents) {
            try {
                // Deserialize event from JSON
                DomainEvent event = eventSerializer.deserialize(eventEntity.getEventData());
                
                // Publish to Kafka
                kafkaEventPublisher.publish(List.of(event));
                
                // Mark as published on success
                outboxRepositoryAdapter.markAsPublished(eventEntity.getId());
                
                log.debug("Successfully published and marked event: id={}, type={}", 
                         eventEntity.getId(), 
                         eventEntity.getEventType());
                
            } catch (Exception e) {
                log.error("Failed to publish event from outbox: id={}, type={}, error={}", 
                         eventEntity.getId(), 
                         eventEntity.getEventType(), 
                         e.getMessage(), 
                         e);
                // Continue processing remaining events - this event will be retried in next cycle
            }
        }
    }
}
