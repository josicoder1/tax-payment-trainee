package com.example.tax_payment.application.service;

import com.example.tax_payment.application.mapper.InvoiceResultMapper;
import com.example.tax_payment.application.port.inbound.ListInvoicesUseCase;
import com.example.tax_payment.application.port.outbound.InvoiceRepositoryPort;
import com.example.tax_payment.application.query.ListInvoicesQuery;
import com.example.tax_payment.application.result.InvoiceSummaryResult;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListInvoicesService implements ListInvoicesUseCase {

    private final InvoiceRepositoryPort repository;
    private final InvoiceResultMapper mapper;

    public ListInvoicesService(
            InvoiceRepositoryPort repository,
            InvoiceResultMapper mapper
    ) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<InvoiceSummaryResult> listInvoices(ListInvoicesQuery query) {

        return repository.findByTaxpayerTin(query.taxpayerTin())
                .stream()
                .map(mapper::toSummaryResult)
                .toList();
    }
}