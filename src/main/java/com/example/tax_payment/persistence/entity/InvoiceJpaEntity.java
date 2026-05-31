package com.example.tax_payment.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "invoices")
@Getter
@Setter
public class InvoiceJpaEntity {

    @Id
    private UUID id;

    private String taxpayerTin;

    private String taxType;

    private LocalDate taxPeriodStart;

    private LocalDate taxPeriodEnd;

    private String taxPeriodFrequency;

    private String currency;

    private BigDecimal principalAmount;

    private BigDecimal interestAmount;

    private BigDecimal penaltyAmount;

    private BigDecimal totalPaidPrincipal;

    private BigDecimal totalPaidInterest;

    private BigDecimal totalPaidPenalty;

    private String status;

    public InvoiceJpaEntity() {
    }

    // getters/setters
}