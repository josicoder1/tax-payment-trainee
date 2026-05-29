package com.example.tax_payment.persistence.mapper;

import com.example.tax_payment.domain.model.Payment;
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
		return new PaymentJpaEntity();
	}

	/**
	 * Converts JPA entity to Payment domain model.
	 */
	public Payment toDomain(PaymentJpaEntity entity) {
		if (entity == null) {
			return null;
		}
		return new Payment();
	}
}
