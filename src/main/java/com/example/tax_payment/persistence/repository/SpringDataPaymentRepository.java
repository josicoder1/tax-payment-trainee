package com.example.tax_payment.persistence.mapper;

import com.example.tax_payment.domain.model.Invoice;
import com.example.tax_payment.domain.valueobject.InvoiceStatus;
import com.example.tax_payment.domain.valueobject.Money;
import com.example.tax_payment.domain.valueobject.TaxPeriod;
import com.example.tax_payment.domain.valueobject.TaxTypeCode;
import com.example.tax_payment.persistence.entity.InvoiceJpaEntity;

public class InvoicePersistenceMapper {

    public InvoiceJpaEntity toEntity(Invoice invoice) {

        InvoiceJpaEntity entity = new InvoiceJpaEntity();

        entity.setId(invoice.getId());
        entity.setTaxpayerTin(invoice.getTaxpayerTin());

        entity.setTaxType(invoice.getTaxType().toString());
        entity.setTaxPeriod(invoice.getTaxPeriod().toString());

        entity.setCurrency(invoice.getCurrency());

        entity.setPrincipalAmount(
                invoice.getPrincipalAmount().getAmount()
        );

        entity.setInterestAmount(
                invoice.getInterestAmount().getAmount()
        );

        entity.setPenaltyAmount(
                invoice.getPenaltyAmount().getAmount()
        );

        entity.setTotalPaidPrincipal(
                invoice.getTotalPaidPrincipal().getAmount()
        );

        entity.setTotalPaidInterest(
                invoice.getTotalPaidInterest().getAmount()
        );

        entity.setTotalPaidPenalty(
                invoice.getTotalPaidPenalty().getAmount()
        );

        entity.setStatus(invoice.getStatus().name());

        return entity;
    }

    public Invoice toDomain(InvoiceJpaEntity entity) {

        Invoice invoice = new Invoice(
                entity.getId(),
                entity.getTaxpayerTin(),
                new TaxTypeCode(entity.getTaxType()),
                TaxPeriod.parse(entity.getTaxPeriod()),
                new Money(entity.getPrincipalAmount(), entity.getCurrency()),
                new Money(entity.getInterestAmount(), entity.getCurrency()),
                new Money(entity.getPenaltyAmount(), entity.getCurrency())
        );

        return invoice;
    }
}