package com.example.tax_payment.domain.model;

import com.example.tax_payment.domain.valueobject.Money;
import com.example.tax_payment.domain.valueobject.PaymentStatus;

import java.time.Instant;
import java.util.UUID;

public class Payment {

    private UUID id;
    private Money amount;
    private String taxpayerId;
    private String taxType;
    private String taxPeriod;
    private PaymentStatus status;
    private Instant createdAt;
    private String referenceNumber;
    private String failureReason;   // added for audit

    public Payment(Money amount, String taxpayerId, String taxType, String taxPeriod) {
        this.id = UUID.randomUUID();
        this.amount = amount;
        this.taxpayerId = taxpayerId;
        this.taxType = taxType;
        this.taxPeriod = taxPeriod;
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

    // Getters
    public UUID getId() { return id; }
    public Money getAmount() { return amount; }
    public String getTaxpayerId() { return taxpayerId; }
    public String getTaxType() { return taxType; }
    public String getTaxPeriod() { return taxPeriod; }
    public PaymentStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public String getReferenceNumber() { return referenceNumber; }
    public String getFailureReason() { return failureReason; }

    // Setters (for reconstruction)
    public void setId(UUID id) { this.id = id; }
    public void setAmount(Money amount) { this.amount = amount; }
    public void setTaxpayerId(String taxpayerId) { this.taxpayerId = taxpayerId; }
    public void setTaxType(String taxType) { this.taxType = taxType; }
    public void setTaxPeriod(String taxPeriod) { this.taxPeriod = taxPeriod; }
    public void setStatus(PaymentStatus status) { this.status = status; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public void setReferenceNumber(String referenceNumber) { this.referenceNumber = referenceNumber; }
    public void setFailureReason(String failureReason) { this.failureReason = failureReason; }
}