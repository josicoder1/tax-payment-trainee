package com.example.tax_payment.application.service;

import com.example.tax_payment.application.command.PayInvoiceCommand;
import com.example.tax_payment.application.mapper.PaymentResultMapper;
import com.example.tax_payment.application.port.inbound.PayInvoiceUseCase;
import com.example.tax_payment.application.port.outbound.EventPublisherPort;
import com.example.tax_payment.application.port.outbound.LedgerEntryRepositoryPort;
import com.example.tax_payment.application.port.outbound.PaymentAuditRepositoryPort;
import com.example.tax_payment.application.port.outbound.InvoiceRepositoryPort;
import com.example.tax_payment.application.port.outbound.PaymentGatewayPort;
import com.example.tax_payment.application.port.outbound.PaymentRepositoryPort;
import com.example.tax_payment.application.port.outbound.TransactionRepositoryPort;
import com.example.tax_payment.application.result.PaymentResult;
import com.example.tax_payment.domain.model.Invoice;
import com.example.tax_payment.domain.model.LedgerEntry;
import com.example.tax_payment.domain.model.Payment;
import com.example.tax_payment.domain.model.Transaction;
import com.example.tax_payment.domain.service.PaymentAllocation;
import com.example.tax_payment.domain.service.PaymentAllocationService;
import com.example.tax_payment.domain.valueobject.Money;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class PayInvoiceService implements PayInvoiceUseCase {

    private final InvoiceAuditService invoiceAuditService;

    private final InvoiceRepositoryPort invoiceRepository;
    private final PaymentRepositoryPort paymentRepository;
    private final PaymentGatewayPort gatewayPort;
    private final EventPublisherPort eventPublisher;
    private final PaymentAuditRepositoryPort paymentAuditRepository;
    private final TransactionRepositoryPort transactionRepository;
    private final LedgerEntryRepositoryPort ledgerEntryRepository;

    private final PaymentAllocationService allocationService;
    private final PaymentResultMapper paymentResultMapper;

    public PayInvoiceService(
            InvoiceRepositoryPort invoiceRepository,
            PaymentRepositoryPort paymentRepository,
            PaymentGatewayPort gatewayPort,
            EventPublisherPort eventPublisher,
            PaymentAuditRepositoryPort paymentAuditRepository,
            TransactionRepositoryPort transactionRepository,
            LedgerEntryRepositoryPort ledgerEntryRepository,
            PaymentAllocationService allocationService,
            PaymentResultMapper paymentResultMapper,
            InvoiceAuditService invoiceAuditService

    ) {
        this.invoiceRepository = invoiceRepository;
        this.paymentRepository = paymentRepository;
        this.gatewayPort = gatewayPort;
        this.eventPublisher = eventPublisher;
        this.paymentAuditRepository = paymentAuditRepository;
        this.transactionRepository = transactionRepository;
        this.ledgerEntryRepository = ledgerEntryRepository;
        this.allocationService = allocationService;
        this.paymentResultMapper = paymentResultMapper;
        this.invoiceAuditService = invoiceAuditService;

    }

    @Override
    public PaymentResult pay(PayInvoiceCommand command) {
        Optional<Payment> existingPayment =
                paymentRepository.findByIdempotencyKey(
                        command.idempotencyKey()
                );

        if (existingPayment.isPresent()) {
            // record duplicate request as audit (no new processing occurs)
            try {
                paymentAuditRepository.saveAudit(
                        null,
                        existingPayment.get().getId(),
                        "DUPLICATE_REQUEST",
                        existingPayment.get().getStatus().name(),
                        existingPayment.get().getStatus().name(),
                        null,
                        command.idempotencyKey(),
                        null
                );
            } catch (Exception ignore) {
                // audit failures should not block normal idempotent response
            }

            return paymentResultMapper.toResult(
                    existingPayment.get(),
                    null
            );
        }

        Invoice invoice = invoiceRepository.findById(command.invoiceId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Invoice not found")
                );



        Money paymentMoney = new Money(
                command.amount(),
                command.currency()
        );

        // Payment can still carry TIN for audit (OK)
        Payment payment = new Payment(
                paymentMoney,
                invoice.getTaxpayerTin(),
                invoice.getTaxType().toString(),
                invoice.getTaxPeriod().toString(),
                command.idempotencyKey()
        );

        // audit: payment requested
        try {
            paymentAuditRepository.saveAudit(
                    null,
                    payment.getId(),
                    "REQUESTED",
                    null,
                    payment.getStatus().name(),
                    null,
                    command.idempotencyKey(),
                    null
            );
        } catch (Exception ignore) {
            // do not fail the flow for audit write issues
        }

        boolean success = gatewayPort.process(payment);

        if (!success) {
            payment.markFailed("Gateway processing failed");
            paymentRepository.save(payment);
            try {
                paymentAuditRepository.saveAudit(
                        null,
                        payment.getId(),
                        "FAILED",
                        "PENDING",
                        payment.getStatus().name(),
                        payment.getFailureReason(),
                        payment.getIdempotencyKey(),
                        null
                );
            } catch (Exception ignore) {
            }
            return paymentResultMapper.toResult(payment, null);
        }

        PaymentAllocation allocation =
                allocationService.allocate(invoice, paymentMoney);
        String oldStatus =
                invoice.getStatus().name();

        invoice.applyPayment(allocation);
        String newStatus = invoice.getStatus().name();

        if (!oldStatus.equals(newStatus)) {
            invoiceAuditService.audit(
                    invoice.getId(),
                    oldStatus,
                    newStatus
            );
        }

        payment.markSuccess();

        invoiceRepository.save(invoice);
        paymentRepository.save(payment);

        Transaction transaction = Transaction.paymentReceived(
                invoice.getId(),
                payment.getId(),
                paymentMoney
        );
        transactionRepository.save(transaction);
        ledgerEntryRepository.saveAll(
                LedgerEntry.forPaymentReceived(
                        invoice.getId(),
                        payment.getId(),
                        transaction.getId(),
                        allocation,
                        paymentMoney
                )
        );

        eventPublisher.publish(invoice.pullDomainEvents());

        // audit success with allocation details
        try {
            String payload = allocation == null ? null :
                    String.format(
                            "penalty=%s,interest=%s,principal=%s",
                            allocation.penaltyAllocated(),
                            allocation.interestAllocated(),
                            allocation.principalAllocated()
                    );

            paymentAuditRepository.saveAudit(
                    null,
                    payment.getId(),
                    "SUCCESS",
                    "PENDING",
                    payment.getStatus().name(),
                    null,
                    payment.getIdempotencyKey(),
                    payload
            );
        } catch (Exception ignore) {
        }

        return paymentResultMapper.toResult(payment, allocation);
    }
}
