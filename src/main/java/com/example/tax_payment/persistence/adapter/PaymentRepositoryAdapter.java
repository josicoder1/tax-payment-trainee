package com.example.tax_payment.persistence.adapter;

import com.example.tax_payment.application.port.outbound.PaymentRepositoryPort;
import com.example.tax_payment.domain.model.Payment;
import com.example.tax_payment.persistence.mapper.PaymentPersistenceMapper;
import com.example.tax_payment.persistence.repository.SpringDataPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryAdapter
        implements PaymentRepositoryPort {

    private final SpringDataPaymentRepository repository;
    private final PaymentPersistenceMapper mapper;

    @Override
    public Payment save(Payment payment) {

        return mapper.toDomain(
                repository.save(
                        mapper.toEntity(payment)
                )
        );
    }

    @Override
    public Optional<Payment> findById(UUID id) {

        return repository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Payment> findByIdempotencyKey(
            String idempotencyKey
    ) {

        return repository
                .findByIdempotencyKey(idempotencyKey)
                .map(mapper::toDomain);
    }
}