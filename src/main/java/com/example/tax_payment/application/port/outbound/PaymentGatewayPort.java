package com.example.tax_payment.application.port.outbound;

import com.example.tax_payment.domain.model.Payment;

public interface PaymentGatewayPort {

    boolean process(Payment payment);
}