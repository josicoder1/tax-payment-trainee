package com.example.tax_payment.persistence.mapper;

import com.example.tax_payment.domain.model.Payment;
import com.example.tax_payment.domain.valueobject.Money;
import com.example.tax_payment.domain.valueobject.PaymentStatus;
import com.example.tax_payment.persistence.entity.PaymentJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class PaymentPersistenceMapper {

    public PaymentJpaEntity toEntity(Payment payment) {

        PaymentJpaEntity entity = new PaymentJpaEntity();

        entity.setId(payment.getId());

        entity.setAmount(
                payment.getAmount().getAmount()
        );
        entity.setIdempotencyKey(
                payment.getIdempotencyKey()
        );

        entity.setCurrency(
                payment.getAmount().getCurrency()
        );

        entity.setTaxpayerId(
                payment.getTaxpayerId()
        );

        entity.setTaxType(
                payment.getTaxType()
        );

        entity.setTaxPeriod(
                payment.getTaxPeriod()
        );

        entity.setStatus(
                payment.getStatus().name()
        );

        entity.setCreatedAt(
                payment.getCreatedAt()
        );

        entity.setReferenceNumber(
                payment.getReferenceNumber()
        );

        entity.setFailureReason(
                payment.getFailureReason()
        );

        return entity;
    }

    public Payment toDomain(PaymentJpaEntity entity) {

        Payment payment =
                new Payment(
                        new Money(
                                entity.getAmount(),
                                entity.getCurrency()
                        ),
                        entity.getTaxpayerId(),
                        entity.getTaxType(),
                        entity.getTaxPeriod(),
                        entity.getIdempotencyKey()
                );

        payment.setId(entity.getId());

        payment.setStatus(
                PaymentStatus.valueOf(
                        entity.getStatus()
                )
        );

        payment.setCreatedAt(
                entity.getCreatedAt()
        );
        payment.setIdempotencyKey(
                entity.getIdempotencyKey()
        );
        payment.setReferenceNumber(
                entity.getReferenceNumber()
        );

        payment.setFailureReason(
                entity.getFailureReason()
        );

        return payment;
    }
}