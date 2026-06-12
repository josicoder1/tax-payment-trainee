package com.example.tax_payment.api.controller;

import com.example.tax_payment.application.port.inbound.GenerateInvoicePdfUseCase;
import com.example.tax_payment.application.port.inbound.GeneratePaymentReceiptPdfUseCase;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
public class PdfController {

    private final GenerateInvoicePdfUseCase invoiceUseCase;
    private final GeneratePaymentReceiptPdfUseCase paymentUseCase;

    public PdfController(
            GenerateInvoicePdfUseCase invoiceUseCase,
            GeneratePaymentReceiptPdfUseCase paymentUseCase) {

        this.invoiceUseCase = invoiceUseCase;
        this.paymentUseCase = paymentUseCase;
    }

    @GetMapping("/invoices/{id}/pdf")

    public ResponseEntity<byte[]> invoice(@PathVariable UUID id) {

        byte[] pdf = invoiceUseCase.generateInvoicePdf(id);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=invoice.pdf")
                .body(pdf);
    }

    @GetMapping("/payments/{id}/receipt")
    public ResponseEntity<byte[]> payment(@PathVariable UUID id) {

        byte[] pdf = paymentUseCase.generatePaymentReceiptPdf(id);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=receipt.pdf")
                .body(pdf);
    }
}