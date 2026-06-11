package com.example.tax_payment.application.port.outbound;

import com.example.tax_payment.domain.model.Payment;

public interface PaymentReceiptPdfPort {
    byte[] generate(Payment payment);
}