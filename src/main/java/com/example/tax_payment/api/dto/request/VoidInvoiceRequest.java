package com.example.tax_payment.api.dto.request;

import java.util.UUID;

public record VoidInvoiceRequest(
        UUID invoiceId
) {}