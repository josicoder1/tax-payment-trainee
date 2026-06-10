package com.example.tax_payment.application.service;

import com.example.tax_payment.application.port.inbound.ListPaymentAuditUseCase;
import com.example.tax_payment.application.port.outbound.PaymentAuditRepositoryPort;
import com.example.tax_payment.application.result.PaymentAuditResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ListPaymentAuditService implements ListPaymentAuditUseCase {

    private final PaymentAuditRepositoryPort paymentAuditRepository;

    public ListPaymentAuditService(PaymentAuditRepositoryPort paymentAuditRepository) {
        this.paymentAuditRepository = paymentAuditRepository;
    }

    @Override
    public List<PaymentAuditResult> listByPaymentId(UUID paymentId) {
        return paymentAuditRepository.findByPaymentId(paymentId);
    }
}
