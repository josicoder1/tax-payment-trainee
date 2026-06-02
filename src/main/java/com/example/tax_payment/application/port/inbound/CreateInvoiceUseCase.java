package com.example.tax_payment.application.port.inbound;

import com.example.tax_payment.application.command.CreateInvoiceCommand;
import com.example.tax_payment.application.result.InvoiceResult;

public interface CreateInvoiceUseCase {

    InvoiceResult create(CreateInvoiceCommand command);
}