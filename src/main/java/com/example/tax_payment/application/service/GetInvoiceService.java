package com.example.tax_payment.application.service;

import com.example.tax_payment.application.mapper.InvoiceResultMapper;
import com.example.tax_payment.application.port.inbound.GetInvoiceUseCase;
import com.example.tax_payment.application.port.outbound.InvoiceRepositoryPort;
import com.example.tax_payment.application.query.GetInvoiceQuery;
import com.example.tax_payment.application.result.InvoiceResult;

public class GetInvoiceService implements GetInvoiceUseCase {

    private final InvoiceRepositoryPort repository;
    private final InvoiceResultMapper mapper;

    public GetInvoiceService(
            InvoiceRepositoryPort repository,
            InvoiceResultMapper mapper
    ) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public InvoiceResult getInvoice(GetInvoiceQuery query) {

        return repository.findById(query.invoiceId())
                .map(mapper::toResult)
                .orElseThrow(() ->
                        new IllegalArgumentException("Invoice not found")
                );
    }
}