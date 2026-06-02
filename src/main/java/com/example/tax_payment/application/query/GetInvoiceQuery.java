package com.example.tax_payment.application.query;

import java.util.UUID;

public record GetInvoiceQuery(
        UUID invoiceId
) {
}