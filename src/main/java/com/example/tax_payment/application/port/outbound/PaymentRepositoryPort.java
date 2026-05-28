package com.example.tax_payment.application.port.outbound;

import com.example.tax_payment.domain.model.Payment;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepositoryPort {

	Payment save(Payment payment);

	Optional<Payment> findById(UUID id);

	boolean existsById(UUID paymentId);
}
