package com.example.tax_payment.application.port.inbound;

import com.example.tax_payment.application.result.LedgerEntryResult;

import java.util.List;
import java.util.UUID;

public interface ListLedgerEntriesUseCase {

    List<LedgerEntryResult> listAll();

    List<LedgerEntryResult> listByInvoice(UUID invoiceId);
}
