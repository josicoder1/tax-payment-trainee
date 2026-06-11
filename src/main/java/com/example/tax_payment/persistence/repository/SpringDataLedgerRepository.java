package com.example.tax_payment.persistence.repository;

import com.example.tax_payment.persistence.entity.LedgerEntryJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;
public interface SpringDataLedgerRepository
        extends JpaRepository<LedgerEntryJpaEntity, UUID> {

    List<LedgerEntryJpaEntity> findAllByOrderByCreatedAtDesc();
}