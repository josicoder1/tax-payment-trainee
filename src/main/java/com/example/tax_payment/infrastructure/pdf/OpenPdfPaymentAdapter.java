package com.example.tax_payment.infrastructure.pdf;


import com.example.tax_payment.application.port.outbound.PaymentReceiptPdfPort;
import com.example.tax_payment.domain.model.Payment;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
@Component
public class OpenPdfPaymentAdapter implements PaymentReceiptPdfPort {

    @Override
    public byte[] generate(Payment payment) {

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);

            document.open();

            document.add(new Paragraph("PAYMENT RECEIPT"));
            document.add(new Paragraph("Payment ID: " + payment.getId()));
            document.add(new Paragraph("Reference: " + payment.getReferenceNumber()));
            document.add(new Paragraph("Taxpayer: " + payment.getTaxpayerId()));
            document.add(new Paragraph("Amount: " + payment.getAmount().amount()));
            document.add(new Paragraph("Currency: " + payment.getAmount().currency()));
            document.add(new Paragraph("Status: " + payment.getStatus()));

            document.close();

            return outputStream.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate payment PDF", e);
        }
    }
}