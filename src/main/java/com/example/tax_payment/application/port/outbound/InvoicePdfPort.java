package com.example.tax_payment.application.port.outbound;

import com.example.tax_payment.domain.model.Invoice;

public interface InvoicePdfPort {
    byte[] generate(Invoice invoice);
}