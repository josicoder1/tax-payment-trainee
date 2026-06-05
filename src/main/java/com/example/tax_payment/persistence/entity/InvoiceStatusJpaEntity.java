package com.example.tax_payment.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "invoice_status")
@Getter
@Setter
@NoArgsConstructor
public class InvoiceStatusJpaEntity {

    @Id
    private Long id;

    private String code;

    private String description;
}