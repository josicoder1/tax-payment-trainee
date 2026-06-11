package com.example.tax_payment.application.port.inbound;

import java.util.UUID;

public interface GenerateInvoicePdfUseCase {

    byte[] generateInvoicePdf(UUID invoiceId);
}