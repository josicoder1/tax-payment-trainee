package com.example.tax_payment.persistence.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "invoices")
public class InvoiceJpaEntity {

    @Id
    private UUID id;

    private String taxpayerTin;

    private String taxType;

    private String taxPeriod;

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