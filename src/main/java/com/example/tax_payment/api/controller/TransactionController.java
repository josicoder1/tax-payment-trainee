package com.example.tax_payment.api.controller;

import com.example.tax_payment.api.dto.response.TransactionResponse;
import com.example.tax_payment.application.port.inbound.ListTransactionsUseCase;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final ListTransactionsUseCase listTransactionsUseCase;

    public TransactionController(
            ListTransactionsUseCase listTransactionsUseCase
    ) {
        this.listTransactionsUseCase = listTransactionsUseCase;
    }

    @GetMapping
    public List<TransactionResponse> listAll() {
        return listTransactionsUseCase.listAll().stream()
                .map(result -> new TransactionResponse(
                        result.id(),
                        result.invoiceId(),
                        result.paymentId(),
                        result.type(),
                        result.description(),
                        result.amount(),
                        result.currency(),
                        result.createdAt()
                ))
                .toList();
    }

    @GetMapping("/by-invoice/{invoiceId}")
    public List<TransactionResponse> listByInvoice(
            @PathVariable UUID invoiceId
    ) {
        return listTransactionsUseCase.listByInvoice(invoiceId).stream()
                .map(result -> new TransactionResponse(
                        result.id(),
                        result.invoiceId(),
                        result.paymentId(),
                        result.type(),
                        result.description(),
                        result.amount(),
                        result.currency(),
                        result.createdAt()
                ))
                .toList();
    }
}
