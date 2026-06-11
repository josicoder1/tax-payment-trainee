package com.example.tax_payment.application.port.inbound;

import java.util.UUID;

public interface GeneratePaymentReceiptPdfUseCase {

    byte[] generatePaymentReceiptPdf(UUID paymentId);
}