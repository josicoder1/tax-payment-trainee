package com.example.tax_payment.application.service;

import com.example.tax_payment.application.port.inbound.GeneratePaymentReceiptPdfUseCase;
import com.example.tax_payment.application.port.outbound.PaymentReceiptPdfPort;
import com.example.tax_payment.application.port.outbound.PaymentRepositoryPort;
import com.example.tax_payment.domain.exception.PaymentNotFoundException;
import com.example.tax_payment.domain.model.Payment;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GeneratePaymentReceiptPdfService
        implements GeneratePaymentReceiptPdfUseCase {

    private final PaymentRepositoryPort paymentRepositoryPort;
    private final PaymentReceiptPdfPort paymentReceiptPdfPort;

    public GeneratePaymentReceiptPdfService(
            PaymentRepositoryPort paymentRepositoryPort,
            PaymentReceiptPdfPort paymentReceiptPdfPort) {

        this.paymentRepositoryPort = paymentRepositoryPort;
        this.paymentReceiptPdfPort = paymentReceiptPdfPort;
    }

    @Override
    public byte[] generatePaymentReceiptPdf(UUID paymentId) {

        Payment payment = paymentRepositoryPort.findById(paymentId)
                .orElseThrow(() ->
                        new PaymentNotFoundException(paymentId));

        return paymentReceiptPdfPort.generate(payment);
    }
}