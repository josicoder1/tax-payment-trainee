package com.example.tax_payment.application.port.outbound;

import java.util.UUID;

public interface PaymentAuditRepositoryPort {

    void saveAudit(
            UUID id,
            UUID paymentId,
            String eventType,
            String oldStatus,
            String newStatus,
            String reason,
            String idempotencyKey,
            String payload
    );

}
