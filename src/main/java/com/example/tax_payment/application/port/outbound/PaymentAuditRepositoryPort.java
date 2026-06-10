package com.example.tax_payment.application.port.outbound;

import com.example.tax_payment.application.result.PaymentAuditResult;

import java.util.List;
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

    List<PaymentAuditResult> findByPaymentId(UUID paymentId);
}
