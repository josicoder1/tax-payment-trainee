package com.example.tax_payment.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "ledger_entries")
@Getter
@Setter
@NoArgsConstructor
public class LedgerEntryJpaEntity {

    @Id
    private UUID id;

    private UUID invoiceId;

    private UUID paymentId;

    private UUID transactionId;

    private String account;

    private String entrySide;

    private BigDecimal amount;

    private String currency;

    private String description;

    private Instant createdAt;
}
