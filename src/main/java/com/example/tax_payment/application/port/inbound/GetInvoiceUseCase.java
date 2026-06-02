package com.example.tax_payment.application.port.inbound;

import com.example.tax_payment.application.query.GetInvoiceQuery;
import com.example.tax_payment.application.result.InvoiceResult;

public interface GetInvoiceUseCase {

    InvoiceResult getInvoice(GetInvoiceQuery query);
}