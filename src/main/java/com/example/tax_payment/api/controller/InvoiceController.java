package com.example.tax_payment.api.controller;

import com.example.tax_payment.application.port.inbound.GetInvoiceUseCase;
import com.example.tax_payment.application.query.GetInvoiceQuery;
import com.example.tax_payment.application.result.InvoiceResult;
import com.example.tax_payment.api.dto.response.InvoiceResponse;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final GetInvoiceUseCase getInvoiceUseCase;

    public InvoiceController(GetInvoiceUseCase getInvoiceUseCase) {
        this.getInvoiceUseCase = getInvoiceUseCase;
    }

    @GetMapping("/{id}")
    public InvoiceResponse get(@PathVariable UUID id) {

        InvoiceResult result = getInvoiceUseCase.getInvoice(
                new GetInvoiceQuery(id)
        );

        return new InvoiceResponse(
                result.invoiceId(),
                result.taxpayerTin(),
                result.taxType(),
                result.taxPeriod(),
                result.status(),
                result.principalAmount(),
                result.interestAmount(),
                result.penaltyAmount(),
                result.paidPrincipal(),
                result.paidInterest(),
                result.paidPenalty(),
                result.outstandingAmount(),
                result.currency()
        );
    }
}