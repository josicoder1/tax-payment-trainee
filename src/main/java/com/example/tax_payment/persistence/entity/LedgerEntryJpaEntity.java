package com.example.tax_payment.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "ledger_entries")
@Getter
@Setter
public class LedgerEntryJpaEntity {

    @Id
    private UUID id;

    private UUID transactionId;
    private UUID paymentId;
    private UUID invoiceId;

    private String account;
    private String type;

    private java.math.BigDecimal amount;
    private String currency;

    private Instant createdAt;
}