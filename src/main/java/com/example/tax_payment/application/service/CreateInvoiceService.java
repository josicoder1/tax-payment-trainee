package com.example.tax_payment.application.service;

import com.example.tax_payment.application.command.CreateInvoiceCommand;
import com.example.tax_payment.application.mapper.InvoiceResultMapper;
import com.example.tax_payment.application.port.inbound.CreateInvoiceUseCase;
import com.example.tax_payment.application.port.outbound.InvoiceRepositoryPort;
import com.example.tax_payment.application.port.outbound.LedgerEntryRepositoryPort;
import com.example.tax_payment.application.port.outbound.TransactionRepositoryPort;
import com.example.tax_payment.application.result.InvoiceResult;
import com.example.tax_payment.domain.model.Invoice;
import com.example.tax_payment.domain.model.LedgerEntry;
import com.example.tax_payment.domain.model.Transaction;
import com.example.tax_payment.domain.valueobject.Money;
import com.example.tax_payment.domain.valueobject.TaxPeriod;
import com.example.tax_payment.domain.valueobject.TaxTypeCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.UUID;

@Service
@Transactional
public class CreateInvoiceService
        implements CreateInvoiceUseCase {

    private final InvoiceRepositoryPort repository;
    private final TransactionRepositoryPort transactionRepository;
    private final LedgerEntryRepositoryPort ledgerEntryRepository;
    private final InvoiceResultMapper mapper;

    public CreateInvoiceService(
            InvoiceRepositoryPort repository,
            TransactionRepositoryPort transactionRepository,
            LedgerEntryRepositoryPort ledgerEntryRepository,
            InvoiceResultMapper mapper
    ) {
        this.repository = repository;
        this.transactionRepository = transactionRepository;
        this.ledgerEntryRepository = ledgerEntryRepository;
        this.mapper = mapper;
    }

    @Override
    public InvoiceResult create(
            CreateInvoiceCommand command
    ) {

        Invoice invoice = new Invoice(
                UUID.randomUUID(),
                command.taxpayerTin(),
                new TaxTypeCode(
                        command.taxTypeCode()
                ),
                TaxPeriod.monthly(
                        YearMonth.of(
                                command.taxYear(),
                                command.taxMonth()
                        )
                ),
                new Money(
                        command.principalAmount(),
                        command.currency()
                ),
                new Money(
                        command.interestAmount(),
                        command.currency()
                ),
                new Money(
                        command.penaltyAmount(),
                        command.currency()
                )
        );

        Invoice saved = repository.save(invoice);

        Transaction transaction = Transaction.invoiceCreated(
                saved.getId(),
                saved.getPrincipalAmount()
                        .add(saved.getInterestAmount())
                        .add(saved.getPenaltyAmount())
        );
        transactionRepository.save(transaction);
        ledgerEntryRepository.saveAll(
                LedgerEntry.forInvoiceCreated(saved, transaction.getId())
        );

        return mapper.toResult(saved);
    }
}