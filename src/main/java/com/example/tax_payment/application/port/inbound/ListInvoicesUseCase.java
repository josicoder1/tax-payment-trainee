package com.example.tax_payment.application.port.inbound;

import com.example.tax_payment.application.query.ListInvoicesQuery;
import com.example.tax_payment.application.result.InvoiceSummaryResult;

import java.util.List;

public interface ListInvoicesUseCase {

    List<InvoiceSummaryResult> listInvoices(ListInvoicesQuery query);
}