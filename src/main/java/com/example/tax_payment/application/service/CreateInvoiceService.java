package com.example.tax_payment.application.service;

import com.example.tax_payment.application.command.CreateInvoiceCommand;
import com.example.tax_payment.application.mapper.InvoiceResultMapper;
import com.example.tax_payment.application.port.inbound.CreateInvoiceUseCase;
import com.example.tax_payment.application.port.outbound.InvoiceNumberGeneratorPort;
import com.example.tax_payment.application.port.outbound.InvoiceRepositoryPort;
import com.example.tax_payment.application.port.outbound.TransactionRepositoryPort;
import com.example.tax_payment.application.result.InvoiceResult;
import com.example.tax_payment.domain.model.Invoice;
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
    private final InvoiceNumberGeneratorPort invoiceNumberGenerator;
    private final InvoiceResultMapper mapper;

    public CreateInvoiceService(
            InvoiceRepositoryPort repository,
            TransactionRepositoryPort transactionRepository,
            InvoiceNumberGeneratorPort invoiceNumberGenerator,
            InvoiceResultMapper mapper
    ) {
        this.repository = repository;
        this.transactionRepository = transactionRepository;
        this.invoiceNumberGenerator = invoiceNumberGenerator;
        this.mapper = mapper;
    }

    @Override
    public InvoiceResult create(
            CreateInvoiceCommand command
    ) {

        YearMonth taxPeriod = YearMonth.of(command.taxYear(), command.taxMonth());

        Invoice invoice = new Invoice(
                UUID.randomUUID(),
                invoiceNumberGenerator.nextInvoiceNumber(taxPeriod),
                command.taxpayerTin(),
                new TaxTypeCode(
                        command.taxTypeCode()
                ),
                TaxPeriod.monthly(taxPeriod),
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

        transactionRepository.save(
                Transaction.invoiceCreated(
                        saved.getId(),
                        saved.getPrincipalAmount()
                                .add(saved.getInterestAmount())
                                .add(saved.getPenaltyAmount())
                )
        );

        return mapper.toResult(saved);
    }
}