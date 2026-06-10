package com.example.tax_payment.application.port.inbound;

import com.example.tax_payment.application.result.TransactionResult;

import java.util.List;
import java.util.UUID;

public interface ListTransactionsUseCase {

    List<TransactionResult> listAll();

    List<TransactionResult> listByInvoice(UUID invoiceId);
}
