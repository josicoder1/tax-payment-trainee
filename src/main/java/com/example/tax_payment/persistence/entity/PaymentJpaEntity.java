package com.example.tax_payment.persistence.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "payments")
public class PaymentJpaEntity {

    @Id
    private UUID id;

    private BigDecimal amount;

    private String currency;

    private String taxpayerId;

    private String taxType;

    private String taxPeriod;

    private String status;

    private Instant createdAt;

    private String referenceNumber;

    private String failureReason;

    public PaymentJpaEntity() {
    }

    // getters/setters
}