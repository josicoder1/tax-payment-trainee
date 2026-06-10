package com.example.tax_payment.persistence.adapter;

import com.example.tax_payment.application.port.outbound.PaymentAuditRepositoryPort;
import com.example.tax_payment.persistence.entity.PaymentAuditJpaEntity;
import com.example.tax_payment.persistence.repository.SpringDataPaymentAuditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PaymentAuditRepositoryAdapter implements PaymentAuditRepositoryPort {

    private final SpringDataPaymentAuditRepository repository;

    @Override
    public void saveAudit(
            UUID id,
            UUID paymentId,
            String eventType,
            String oldStatus,
            String newStatus,
            String reason,
            String idempotencyKey,
            String payload
    ) {

        PaymentAuditJpaEntity entity = new PaymentAuditJpaEntity();
        entity.setId(id == null ? UUID.randomUUID() : id);
        entity.setPaymentId(paymentId);
        entity.setEventType(eventType);
        entity.setOldStatus(oldStatus);
        entity.setNewStatus(newStatus);
        entity.setReason(reason);
        entity.setIdempotencyKey(idempotencyKey);
        entity.setPayload(payload);
        entity.setCreatedAt(Instant.now());

        repository.save(entity);
    }
}
