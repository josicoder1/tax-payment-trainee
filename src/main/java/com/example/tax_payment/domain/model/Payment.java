package com.example.tax_payment.domain.model;

import com.example.tax_payment.domain.valueobject.Money;
import com.example.tax_payment.domain.valueobject.PaymentStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Setter
@Getter
public class Payment {
    private UUID id;
    private Money amount;
    private String taxpayerId;
    private String taxType;
    private String taxPeriod;
    private PaymentStatus status;
    private Instant createdAt;
    private String referenceNumber;
    private String failureReason;
    // Setters (for reconstruction)
    // Getters
    String idempotencyKey;
    // added for audit

    public Payment(Money amount, String taxpayerId, String taxType, String taxPeriod, String idempotencyKey) {
        this.id = UUID.randomUUID();
        this.amount = amount;
        this.taxpayerId = taxpayerId;
        this.taxType = taxType;
        this.taxPeriod = taxPeriod;
        this.idempotencyKey = idempotencyKey;
        this.status = PaymentStatus.PENDING;
        this.createdAt = Instant.now();
        this.referenceNumber = "REF-" + System.currentTimeMillis();

    }

    public void markSuccess() {
        if (this.status != PaymentStatus.PENDING) {
            throw new IllegalStateException("Only pending payments can be marked success");
        }
        this.status = PaymentStatus.SUCCESS;
        this.failureReason = null;
    }

    public void markFailed(String reason) {
        if (this.status != PaymentStatus.PENDING) {
            throw new IllegalStateException("Only pending payments can be marked failed");
        }
        this.status = PaymentStatus.FAILED;
        this.failureReason = reason;
    }

}