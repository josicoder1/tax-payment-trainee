package com.example.tax_payment.application.command;

import java.util.UUID;

public record VoidInvoiceCommand(
        UUID invoiceId
) {
}