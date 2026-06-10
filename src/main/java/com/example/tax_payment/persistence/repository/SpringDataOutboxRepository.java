package com.example.tax_payment.persistence.repository;

import com.example.tax_payment.persistence.entity.OutboxEventJpaEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SpringDataOutboxRepository
        extends JpaRepository<OutboxEventJpaEntity, UUID> {

    List<OutboxEventJpaEntity> findByPublishedFalseOrderByCreatedAtAsc(Pageable pageable);
}
