package com.example.tax_payment.application.service;

import com.example.tax_payment.application.command.PayInvoiceCommand;
import com.example.tax_payment.application.mapper.PaymentResultMapper;
import com.example.tax_payment.application.port.inbound.PayInvoiceUseCase;
import com.example.tax_payment.application.port.outbound.*;
import com.example.tax_payment.application.result.PaymentResult;
import com.example.tax_payment.domain.model.Invoice;
import com.example.tax_payment.domain.model.Payment;
import com.example.tax_payment.domain.model.Transaction;
import com.example.tax_payment.domain.service.PaymentAllocation;
import com.example.tax_payment.domain.service.PaymentAllocationService;
import com.example.tax_payment.domain.valueobject.Money;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    private final PaymentAllocationService allocationService;
    private final PaymentResultMapper paymentResultMapper;
    private final LedgerService ledgerService;

    public PayInvoiceService(
            InvoiceRepositoryPort invoiceRepository,
            PaymentRepositoryPort paymentRepository,
            PaymentGatewayPort gatewayPort,
            EventPublisherPort eventPublisher,
            PaymentAuditRepositoryPort paymentAuditRepository,
            TransactionRepositoryPort transactionRepository,
            PaymentAllocationService allocationService,
            PaymentResultMapper paymentResultMapper,
            InvoiceAuditService invoiceAuditService,
            LedgerService ledgerService
    ) {
        this.invoiceRepository = invoiceRepository;
        this.paymentRepository = paymentRepository;
        this.gatewayPort = gatewayPort;
        this.eventPublisher = eventPublisher;
        this.paymentAuditRepository = paymentAuditRepository;
        this.transactionRepository = transactionRepository;
        this.allocationService = allocationService;
        this.paymentResultMapper = paymentResultMapper;
        this.invoiceAuditService = invoiceAuditService;
        this.ledgerService = ledgerService;
    }

    @Override
    public PaymentResult pay(PayInvoiceCommand command) {

        Optional<Payment> existingPayment =
                paymentRepository.findByIdempotencyKey(command.idempotencyKey());

        if (existingPayment.isPresent()) {

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
            } catch (Exception ignored) {}

            return new PaymentResult(
                    existingPayment.get().getId(),
                    existingPayment.get().getReferenceNumber(),
                    "ALREADY_PAID",
                    null,
                    "Payment already processed",
                    existingPayment.get().getCreatedAt(),
                    null
            );
        }

        Invoice invoice = invoiceRepository.findById(command.invoiceId())
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found"));

        Money paymentMoney = new Money(command.amount(), command.currency());

        Payment payment = new Payment(
                paymentMoney,
                invoice.getTaxpayerTin(),
                invoice.getTaxType().toString(),
                invoice.getTaxPeriod().toString(),
                command.idempotencyKey()
        );

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
        } catch (Exception ignored) {}

        boolean success = gatewayPort.process(payment);

        if (!success) {

            payment.markFailed("Gateway processing failed");
            Payment failedPayment = paymentRepository.save(payment);

            try {
                paymentAuditRepository.saveAudit(
                        null,
                        failedPayment.getId(),
                        "FAILED",
                        "PENDING",
                        failedPayment.getStatus().name(),
                        failedPayment.getFailureReason(),
                        failedPayment.getIdempotencyKey(),
                        null
                );
            } catch (Exception ignored) {}

            return paymentResultMapper.toResult(failedPayment, null);
        }

        PaymentAllocation allocation =
                allocationService.allocate(invoice, paymentMoney);

        String oldStatus = invoice.getStatus().name();
        invoice.applyPayment(allocation);
        String newStatus = invoice.getStatus().name();

        if (!oldStatus.equals(newStatus)) {
            invoiceAuditService.audit(invoice.getId(), oldStatus, newStatus);
        }

        payment.markSuccess();

        // 1. save payment FIRST
        Payment savedPayment = paymentRepository.save(payment);

        // 2. create transaction ONCE
        Transaction tx = Transaction.paymentReceived(
                invoice.getId(),
                savedPayment.getId(),
                paymentMoney
        );

        transactionRepository.save(tx);

        // 3. ledger entry (financial truth)
        ledgerService.recordPayment(
                savedPayment,
                tx,
                paymentMoney
        );

        // 4. invoice update
        invoiceRepository.save(invoice);

        // 5. events
        eventPublisher.publish(invoice.pullDomainEvents());

        // 6. audit success
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
                    savedPayment.getId(),
                    "SUCCESS",
                    "PENDING",
                    savedPayment.getStatus().name(),
                    null,
                    savedPayment.getIdempotencyKey(),
                    payload
            );
        } catch (Exception ignored) {}

        return paymentResultMapper.toResult(savedPayment, allocation);
    }
}