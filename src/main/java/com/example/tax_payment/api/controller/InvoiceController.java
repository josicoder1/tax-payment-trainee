package com.example.tax_payment.api.controller;

import com.example.tax_payment.api.dto.request.CreateInvoiceRequest;
import com.example.tax_payment.api.dto.response.InvoiceResponse;
import com.example.tax_payment.api.dto.response.InvoiceSummaryResponse;
import com.example.tax_payment.application.command.CreateInvoiceCommand;
import com.example.tax_payment.application.command.VoidInvoiceCommand;
import com.example.tax_payment.application.port.inbound.CreateInvoiceUseCase;
import com.example.tax_payment.application.port.inbound.GetInvoiceUseCase;
import com.example.tax_payment.application.port.inbound.ListInvoicesUseCase;
import com.example.tax_payment.application.port.inbound.VoidInvoiceUseCase;
import com.example.tax_payment.application.query.GetInvoiceQuery;
import com.example.tax_payment.application.query.ListInvoicesQuery;
import com.example.tax_payment.application.result.InvoiceResult;
import com.example.tax_payment.application.result.InvoiceSummaryResult;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final CreateInvoiceUseCase createInvoiceUseCase;
    private final GetInvoiceUseCase getInvoiceUseCase;
    private final ListInvoicesUseCase listInvoicesUseCase;
    private final VoidInvoiceUseCase voidInvoiceUseCase;

    public InvoiceController(
            CreateInvoiceUseCase createInvoiceUseCase,
            GetInvoiceUseCase getInvoiceUseCase,
            ListInvoicesUseCase listInvoicesUseCase,
            VoidInvoiceUseCase voidInvoiceUseCase
    ) {
        this.createInvoiceUseCase = createInvoiceUseCase;
        this.getInvoiceUseCase = getInvoiceUseCase;
        this.listInvoicesUseCase = listInvoicesUseCase;
        this.voidInvoiceUseCase = voidInvoiceUseCase;
    }

    @PostMapping
    public InvoiceResponse create(
            @Valid @RequestBody CreateInvoiceRequest request
    ) {

        InvoiceResult result =
                createInvoiceUseCase.create(
                        new CreateInvoiceCommand(
                                request.taxpayerTin(),
                                request.taxTypeCode(),
                                request.taxYear(),
                                request.taxMonth(),
                                request.principalAmount(),
                                request.interestAmount(),
                                request.penaltyAmount(),
                                request.currency()
                        )
                );

        return toResponse(result);
    }

    @GetMapping("/{id}")
    public InvoiceResponse get(
            @PathVariable UUID id
    ) {

        InvoiceResult result =
                getInvoiceUseCase.getInvoice(
                        new GetInvoiceQuery(id)
                );

        return toResponse(result);
    }

    @GetMapping
    public List<InvoiceSummaryResponse> list(
            @RequestParam String taxpayerTin
    ) {

        List<InvoiceSummaryResult> results =
                listInvoicesUseCase.listInvoices(
                        new ListInvoicesQuery(
                                taxpayerTin
                        )
                );

        return results.stream()
                .map(result ->
                        new InvoiceSummaryResponse(
                                result.invoiceId(),
                                result.invoiceNumber(),
                                result.taxType(),
                                result.taxPeriod(),
                                result.status(),
                                result.outstandingAmount(),
                                result.currency()
                        )
                )
                .toList();
    }

    @PutMapping("/{id}/void")
    public void voidInvoice(
            @PathVariable UUID id
    ) {

        voidInvoiceUseCase.voidInvoice(
                new VoidInvoiceCommand(id)
        );
    }

    private InvoiceResponse toResponse(
            InvoiceResult result
    ) {

        return new InvoiceResponse(
                result.invoiceId(),
                result.invoiceNumber(),
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