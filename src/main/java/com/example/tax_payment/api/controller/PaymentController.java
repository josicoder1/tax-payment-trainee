package com.example.tax_payment.api.controller;

import com.example.tax_payment.api.dto.request.PayInvoiceRequest;
import com.example.tax_payment.api.dto.response.PaymentResponse;
import com.example.tax_payment.application.command.PayInvoiceCommand;
import com.example.tax_payment.application.port.inbound.PayInvoiceUseCase;
import com.example.tax_payment.application.result.PaymentResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PayInvoiceUseCase payInvoiceUseCase;

    public PaymentController(PayInvoiceUseCase payInvoiceUseCase) {
        this.payInvoiceUseCase = payInvoiceUseCase;
    }

    @PostMapping
    public PaymentResponse pay(@RequestBody PayInvoiceRequest request) {

        PaymentResult result =
                payInvoiceUseCase.pay(

                        new PayInvoiceCommand(
                                request.idempotencyKey(),
                                request.invoiceId(),
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
}
