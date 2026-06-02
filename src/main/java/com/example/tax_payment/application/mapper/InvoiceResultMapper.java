package com.example.tax_payment.application.mapper;

import com.example.tax_payment.application.result.InvoiceResult;
import com.example.tax_payment.application.result.InvoiceSummaryResult;
import com.example.tax_payment.domain.model.Invoice;
import org.springframework.stereotype.Component;

@Component
public class InvoiceResultMapper {

    public InvoiceResult toResult(Invoice invoice) {

        return new InvoiceResult(
                invoice.getId(),
                invoice.getTaxpayerTin(),
                invoice.getTaxType().value(),
                invoice.getTaxPeriod().label(),
                invoice.getStatus().name(),

                invoice.getPrincipalAmount().getAmount(),
                invoice.getInterestAmount().getAmount(),
                invoice.getPenaltyAmount().getAmount(),

                invoice.getTotalPaidPrincipal().getAmount(),
                invoice.getTotalPaidInterest().getAmount(),
                invoice.getTotalPaidPenalty().getAmount(),

                invoice.getTotalOutstanding().getAmount(),

                invoice.getCurrency()
        );
    }

    public InvoiceSummaryResult toSummaryResult(Invoice invoice) {

        return new InvoiceSummaryResult(
                invoice.getId(),
                invoice.getTaxType().value(),
                invoice.getTaxPeriod().label(),
                invoice.getStatus().name(),
                invoice.getTotalOutstanding().getAmount(),
                invoice.getCurrency()
        );
    }
}