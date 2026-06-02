package com.example.tax_payment.application.port.inbound;

import com.example.tax_payment.application.command.PayInvoiceCommand;
import com.example.tax_payment.application.result.PaymentResult;

public interface PayInvoiceUseCase {

    PaymentResult pay(PayInvoiceCommand command);
}