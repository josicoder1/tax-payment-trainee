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
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
public class TransactionJpaEntity {

    @Id
    private UUID id;

    private UUID invoiceId;

    private UUID paymentId;

    private String type;

    private String description;

    private BigDecimal amount;

    private String currency;

    private Instant createdAt;
}
