package com.example.tax_payment.application.port.inbound;

import com.example.tax_payment.application.result.PaymentAuditResult;

import java.util.List;
import java.util.UUID;

public interface ListPaymentAuditUseCase {

    List<PaymentAuditResult> listByPaymentId(UUID paymentId);
}
