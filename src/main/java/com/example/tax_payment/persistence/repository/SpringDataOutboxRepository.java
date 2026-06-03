package com.example.tax_payment.persistence.repository;

import com.example.tax_payment.persistence.entity.OutboxEventJpaEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SpringDataOutboxRepository extends JpaRepository<OutboxEventJpaEntity, UUID> {
    
    /**
     * Find all unpublished outbox events ordered by creation time ascending.
     * 
     * @param pageable pagination and sorting information
     * @return list of unpublished events in chronological order
     */
    List<OutboxEventJpaEntity> findByPublishedFalseOrderByCreatedAtAsc(Pageable pageable);
}
