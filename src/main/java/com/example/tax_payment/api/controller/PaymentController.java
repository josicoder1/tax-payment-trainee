package com.example.tax_payment.api.controller;

import com.example.tax_payment.api.dto.request.PayInvoiceRequest;
import com.example.tax_payment.api.dto.response.PaymentAuditResponse;
import com.example.tax_payment.api.dto.response.PaymentResponse;
import com.example.tax_payment.application.command.PayInvoiceCommand;
import com.example.tax_payment.application.port.inbound.ListPaymentAuditUseCase;
import com.example.tax_payment.application.port.inbound.PayInvoiceUseCase;
import com.example.tax_payment.application.result.PaymentResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PayInvoiceUseCase payInvoiceUseCase;
    private final ListPaymentAuditUseCase listPaymentAuditUseCase;

    public PaymentController(
            PayInvoiceUseCase payInvoiceUseCase,
            ListPaymentAuditUseCase listPaymentAuditUseCase
    ) {
        this.payInvoiceUseCase = payInvoiceUseCase;
        this.listPaymentAuditUseCase = listPaymentAuditUseCase;
    }

    @PostMapping
    public PaymentResponse pay(@RequestBody PayInvoiceRequest request) {

        PaymentResult result =
                payInvoiceUseCase.pay(

                        new PayInvoiceCommand(
                                request.idempotencyKey(),
                                request.invoiceNumber(),
                                request.amount(),
                                request.currency()

                        )
                );

        return new PaymentResponse(
                result.paymentId(),
                result.referenceNumber(),
                result.status(),
                result.failureReason(),
                result.createdAt()

        );
    }

    @GetMapping("/{paymentId}/audit")
    public List<PaymentAuditResponse> listAudit(@PathVariable UUID paymentId) {
        return listPaymentAuditUseCase.listByPaymentId(paymentId).stream()
                .map(result -> new PaymentAuditResponse(
                        result.id(),
                        result.paymentId(),
                        result.eventType(),
                        result.oldStatus(),
                        result.newStatus(),
                        result.reason(),
                        result.idempotencyKey(),
                        result.payload(),
                        result.createdAt()
                ))
                .toList();
    }
}
