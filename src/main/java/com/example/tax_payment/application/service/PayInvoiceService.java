package com.example.tax_payment.application.service;

import com.example.tax_payment.application.command.PayInvoiceCommand;
import com.example.tax_payment.application.mapper.PaymentResultMapper;
import com.example.tax_payment.application.port.inbound.PayInvoiceUseCase;
import com.example.tax_payment.application.port.outbound.EventPublisherPort;
import com.example.tax_payment.application.port.outbound.InvoiceRepositoryPort;
import com.example.tax_payment.application.port.outbound.PaymentGatewayPort;
import com.example.tax_payment.application.port.outbound.PaymentRepositoryPort;
import com.example.tax_payment.application.result.PaymentResult;
import com.example.tax_payment.domain.model.Invoice;
import com.example.tax_payment.domain.model.Payment;
import com.example.tax_payment.domain.service.PaymentAllocation;
import com.example.tax_payment.domain.service.PaymentAllocationService;
import com.example.tax_payment.domain.valueobject.Money;

public class PayInvoiceService implements PayInvoiceUseCase {

    private final InvoiceRepositoryPort invoiceRepository;
    private final PaymentRepositoryPort paymentRepository;
    private final PaymentGatewayPort gatewayPort;
    private final EventPublisherPort eventPublisher;

    private final PaymentAllocationService allocationService;
    private final PaymentResultMapper paymentResultMapper;

    public PayInvoiceService(
            InvoiceRepositoryPort invoiceRepository,
            PaymentRepositoryPort paymentRepository,
            PaymentGatewayPort gatewayPort,
            EventPublisherPort eventPublisher,
            PaymentAllocationService allocationService,
            PaymentResultMapper paymentResultMapper
    ) {
        this.invoiceRepository = invoiceRepository;
        this.paymentRepository = paymentRepository;
        this.gatewayPort = gatewayPort;
        this.eventPublisher = eventPublisher;
        this.allocationService = allocationService;
        this.paymentResultMapper = paymentResultMapper;
    }

    @Override
    public PaymentResult pay(PayInvoiceCommand command) {

        Invoice invoice = invoiceRepository.findOpenInvoice(
                command.taxpayerTin(),
                command.taxType(),
                command.taxPeriod()
        ).orElseThrow(() ->
                new IllegalArgumentException("Invoice not found")
        );

        Money paymentMoney = new Money(
                command.amount(),
                command.currency()
        );

        Payment payment = new Payment(
                paymentMoney,
                command.taxpayerTin(),
                command.taxType(),
                command.taxPeriod()
        );

        boolean success = gatewayPort.process(payment);

        if (!success) {

            payment.markFailed("Gateway processing failed");

            paymentRepository.save(payment);

            return paymentResultMapper.toResult(payment, null);
        }

        PaymentAllocation allocation =
                allocationService.allocate(invoice, paymentMoney);

        invoice.applyPayment(allocation);

        payment.markSuccess();

        invoiceRepository.save(invoice);

        paymentRepository.save(payment);

        eventPublisher.publish(invoice.pullDomainEvents());

        return paymentResultMapper.toResult(payment, allocation);
    }
}