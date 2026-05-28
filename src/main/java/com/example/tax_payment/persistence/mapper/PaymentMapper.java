package com.example.tax_payment.persistence.mapper;

import com.example.tax_payment.domain.model.Payment;
import com.example.tax_payment.domain.model.PaymentStatus;
import com.example.tax_payment.domain.valueobject.Money;
import com.example.tax_payment.domain.valueobject.PaymentAllocation;
import com.example.tax_payment.persistence.entity.PaymentJpaEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between Payment domain model and PaymentJpaEntity.
 */
@Component
public class PaymentMapper {

	/**
	 * Converts Payment domain model to JPA entity.
	 */
	public PaymentJpaEntity toEntity(Payment payment) {
		if (payment == null) {
			return null;
		}

		PaymentAllocation allocation = payment.getAllocation();

		return new PaymentJpaEntity(
				payment.getId(),
				payment.getInvoiceId(),
				payment.getAmount().getAmount(),
				payment.getStatus().name(),
				payment.getBankReference(),
				payment.getActorId(),
				payment.getRequestedAt(),
				payment.getConfirmedAt(),
				allocation != null ? allocation.getPenaltyAmount().getAmount() : null,
				allocation != null ? allocation.getInterestAmount().getAmount() : null,
				allocation != null ? allocation.getPrincipalAmount().getAmount() : null
		);
	}

	/**
	 * Converts JPA entity to Payment domain model.
	 */
	public Payment toDomain(PaymentJpaEntity entity) {
		if (entity == null) {
			return null;
		}

		PaymentAllocation allocation = null;
		if (entity.getAllocatedPenalty() != null) {
			allocation = new PaymentAllocation(
					new Money(entity.getAllocatedPenalty()),
					new Money(entity.getAllocatedInterest()),
					new Money(entity.getAllocatedPrincipal())
			);
		}

		return new Payment(
				entity.getId(),
				entity.getInvoiceId(),
				new Money(entity.getAmount()),
				PaymentStatus.valueOf(entity.getStatus()),
				entity.getBankReference(),
				entity.getActorId(),
				entity.getRequestedAt(),
				entity.getConfirmedAt(),
				allocation
		);
	}
}
