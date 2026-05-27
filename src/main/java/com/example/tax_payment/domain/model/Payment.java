package com.example.tax_payment.domain.model;

import com.example.tax_payment.domain.valueobject.Money;
import com.example.tax_payment.domain.valueobject.PaymentStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public class Payment {

    private UUID id;
    private Money amount;
    private String taxpayerId;
    private String taxType;
    private String taxPeriod;
    private PaymentStatus status;
    private LocalDateTime createdAt;
    private String referenceNumber;

    // Constructor for new payment
    public Payment(Money amount, String taxpayerId, String taxType, String taxPeriod) {

        // Generate unique ID
        this.id = UUID.randomUUID();

        this.amount = amount;
        this.taxpayerId = taxpayerId;
        this.taxType = taxType;
        this.taxPeriod = taxPeriod;
        this.status = PaymentStatus.PENDING;
        this.createdAt = LocalDateTime.now();
        this.referenceNumber = "REF-" + System.currentTimeMillis();
    }

    // Business methods
    public void markSuccess() {
        if (this.status != PaymentStatus.PENDING) {
            throw new IllegalStateException(
                    "Only pending payments can be marked success"
            );
        }
        this.status = PaymentStatus.SUCCESS;
    }

    public void markFailed(String reason) {
        if (this.status != PaymentStatus.PENDING) {
            throw new IllegalStateException(
                    "Only pending payments can be marked failed"
            );
        }
        this.status = PaymentStatus.FAILED;
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public Money getAmount() {
        return amount;
    }

    public String getTaxpayerId() {
        return taxpayerId;
    }

    public String getTaxType() {
        return taxType;
    }

    public String getTaxPeriod() {
        return taxPeriod;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    // Setters (for database reconstruction)
    public void setId(UUID id) {
        this.id = id;
    }

    public void setAmount(Money amount) {
        this.amount = amount;
    }

    public void setTaxpayerId(String taxpayerId) {
        this.taxpayerId = taxpayerId;
    }

    public void setTaxType(String taxType) {
        this.taxType = taxType;
    }

    public void setTaxPeriod(String taxPeriod) {
        this.taxPeriod = taxPeriod;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }
}