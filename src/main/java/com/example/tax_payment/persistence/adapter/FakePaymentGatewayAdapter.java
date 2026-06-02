package com.example.tax_payment.persistence.adapter;

import com.example.tax_payment.application.port.outbound.PaymentGatewayPort;
import com.example.tax_payment.domain.model.Payment;
import org.springframework.stereotype.Component;

@Component
public class FakePaymentGatewayAdapter
        implements PaymentGatewayPort {

    @Override
    public boolean process(
            Payment payment
    ) {
        return true;
    }
}