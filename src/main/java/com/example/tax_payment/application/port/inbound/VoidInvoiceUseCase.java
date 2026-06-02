package com.example.tax_payment.application.port.inbound;

import com.example.tax_payment.application.command.VoidInvoiceCommand;

public interface VoidInvoiceUseCase {

    void voidInvoice(VoidInvoiceCommand command);
}
