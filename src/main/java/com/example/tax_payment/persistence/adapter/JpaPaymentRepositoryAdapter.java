package com.example.tax_payment.persistence.adapter;

import com.example.tax_payment.application.port.outbound.PaymentRepositoryPort;
import com.example.tax_payment.domain.model.Payment;
import com.example.tax_payment.persistence.entity.PaymentJpaEntity;
import com.example.tax_payment.persistence.mapper.PaymentMapper;
import com.example.tax_payment.persistence.repository.SpringDataPaymentRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * JPA adapter implementing PaymentRepositoryPort.
 * Bridges the domain layer with JPA persistence.
 */
@Component
public class JpaPaymentRepositoryAdapter implements PaymentRepositoryPort {

	private final SpringDataPaymentRepository jpaRepository;
	private final PaymentMapper mapper;

	public JpaPaymentRepositoryAdapter(
			SpringDataPaymentRepository jpaRepository,
			PaymentMapper mapper
	) {
		this.jpaRepository = jpaRepository;
		this.mapper = mapper;
	}

	@Override
	public Payment save(Payment payment) {
		PaymentJpaEntity entity = mapper.toEntity(payment);
		PaymentJpaEntity savedEntity = jpaRepository.save(entity);
		return mapper.toDomain(savedEntity);
	}

	@Override
	public Optional<Payment> findById(UUID id) {
		return jpaRepository.findById(id)
				.map(mapper::toDomain);
	}

	@Override
	public boolean existsById(UUID paymentId) {
		return jpaRepository.existsById(paymentId);
	}

	public void delete(Payment payment) {
		jpaRepository.deleteById(payment.getId());
	}
}
