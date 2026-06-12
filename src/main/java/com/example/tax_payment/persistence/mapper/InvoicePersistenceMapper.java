package com.example.tax_payment.persistence.mapper;

import com.example.tax_payment.domain.model.Invoice;
import com.example.tax_payment.domain.valueobject.*;
import com.example.tax_payment.persistence.entity.InvoiceJpaEntity;
import com.example.tax_payment.persistence.repository.SpringDataInvoiceStatusRepository;
import org.springframework.stereotype.Component;

@Component
public class InvoicePersistenceMapper {

    private final SpringDataInvoiceStatusRepository statusRepository;

    public InvoicePersistenceMapper(
            SpringDataInvoiceStatusRepository statusRepository
    ) {
        this.statusRepository = statusRepository;
    }


    public InvoiceJpaEntity toEntity(Invoice invoice) {

        InvoiceJpaEntity entity = new InvoiceJpaEntity();

        entity.setId(invoice.getId());
        entity.setInvoiceNumber(invoice.getInvoiceNumber());
        entity.setTaxpayerTin(invoice.getTaxpayerTin());

        entity.setTaxType(invoice.getTaxType().value());

        entity.setTaxPeriodStart(
                invoice.getTaxPeriod().start()
        );

        entity.setTaxPeriodEnd(
                invoice.getTaxPeriod().end()
        );

        entity.setTaxPeriodFrequency(
                invoice.getTaxPeriod()
                        .frequency()
                        .name()
        );

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

        entity.setStatus(
                statusRepository.findByCode(
                        invoice.getStatus().name()
                ).orElseThrow(
                        () -> new IllegalStateException(
                                "Unknown invoice status: "
                                        + invoice.getStatus()
                        )
                )
        );
        return entity;
    }

    public Invoice toDomain(InvoiceJpaEntity entity) {

        return Invoice.reconstitute(
                entity.getId(),
                entity.getInvoiceNumber(),
                entity.getTaxpayerTin(),

                new TaxTypeCode(
                        entity.getTaxType()
                ),

                new TaxPeriod(
                        entity.getTaxPeriodStart(),
                        entity.getTaxPeriodEnd(),
                        PeriodFrequency.valueOf(
                                entity.getTaxPeriodFrequency()
                        )
                ),

                new Money(
                        entity.getPrincipalAmount(),
                        entity.getCurrency()
                ),

                new Money(
                        entity.getInterestAmount(),
                        entity.getCurrency()
                ),

                new Money(
                        entity.getPenaltyAmount(),
                        entity.getCurrency()
                ),

                new Money(
                        entity.getTotalPaidPrincipal(),
                        entity.getCurrency()
                ),

                new Money(
                        entity.getTotalPaidInterest(),
                        entity.getCurrency()
                ),

                new Money(
                        entity.getTotalPaidPenalty(),
                        entity.getCurrency()
                ),

                InvoiceStatus.valueOf(
                        entity.getStatus().getCode()
                )
        );
    }
}