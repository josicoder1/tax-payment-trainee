package com.example.tax_payment.api.controller;

import com.example.tax_payment.api.dto.response.LedgerEntryResponse;
import com.example.tax_payment.application.port.inbound.ListLedgerEntriesUseCase;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/ledger-entries")
public class LedgerController {

    private final ListLedgerEntriesUseCase listLedgerEntriesUseCase;

    public LedgerController(
            ListLedgerEntriesUseCase listLedgerEntriesUseCase
    ) {
        this.listLedgerEntriesUseCase = listLedgerEntriesUseCase;
    }

    @GetMapping
    public List<LedgerEntryResponse> listAll() {
        return listLedgerEntriesUseCase.listAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/by-invoice/{invoiceId}")
    public List<LedgerEntryResponse> listByInvoice(
            @PathVariable UUID invoiceId
    ) {
        return listLedgerEntriesUseCase.listByInvoice(invoiceId).stream()
                .map(this::toResponse)
                .toList();
    }

    private LedgerEntryResponse toResponse(
            com.example.tax_payment.application.result.LedgerEntryResult result
    ) {
        return new LedgerEntryResponse(
                result.id(),
                result.invoiceId(),
                result.paymentId(),
                result.transactionId(),
                result.account(),
                result.entrySide(),
                result.description(),
                result.amount(),
                result.currency(),
                result.createdAt()
        );
    }
}
