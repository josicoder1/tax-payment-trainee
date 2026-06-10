package com.example.tax_payment.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "payment_audit")
@Getter
@Setter
@NoArgsConstructor
public class PaymentAuditJpaEntity {

    @Id
    private UUID id;

    private UUID paymentId;

    private String eventType;

    private String oldStatus;

    private String newStatus;

    @Column(length = 1024)
    private String reason;

    private String idempotencyKey;

    private String payload;

    private Instant createdAt;

}
