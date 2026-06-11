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

    public PayInvoiceService(
            InvoiceRepositoryPort invoiceRepository,
            PaymentRepositoryPort paymentRepository,
            PaymentGatewayPort gatewayPort,
            EventPublisherPort eventPublisher,
            PaymentAuditRepositoryPort paymentAuditRepository,
            TransactionRepositoryPort transactionRepository,
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
        this.allocationService = allocationService;
        this.paymentResultMapper = paymentResultMapper;
        this.invoiceAuditService = invoiceAuditService;
    }

    @Override
    public PaymentResult pay(PayInvoiceCommand command) {

        // ----------------------------
        // 0. HARD GUARD: IDEMPOTENCY
        // ----------------------------
        if (command.idempotencyKey() == null || command.idempotencyKey().isBlank()) {
            throw new IllegalArgumentException("idempotencyKey is required");
        }

        Optional<Payment> existingPayment =
                paymentRepository.findByIdempotencyKey(command.idempotencyKey());

        if (existingPayment.isPresent()) {

            Payment payment = existingPayment.get();

            try {
                paymentAuditRepository.saveAudit(
                        null,
                        payment.getId(),
                        "DUPLICATE_REQUEST",
                        payment.getStatus().name(),
                        payment.getStatus().name(),
                        null,
                        command.idempotencyKey(),
                        null
                );
            } catch (Exception ignored) {}

            return paymentResultMapper.toResult(payment, null);
        }

        // ----------------------------
        // 1. LOAD INVOICE
        // ----------------------------
        Invoice invoice = invoiceRepository.findById(command.invoiceId())
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found"));

        Money paymentMoney = new Money(
                command.amount(),
                command.currency()
        );

        // ----------------------------
        // 2. CREATE PAYMENT (NEW FLOW)
        // ----------------------------
        Payment payment = Payment.create(
                paymentMoney,
                invoice.getTaxpayerTin(),
                invoice.getTaxType().toString(),
                invoice.getTaxPeriod().toString(),
                command.idempotencyKey()
        );

        // audit REQUESTED
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

        // ----------------------------
        // 3. CALL GATEWAY
        // ----------------------------
        boolean success = gatewayPort.process(payment);

        if (!success) {
            payment.markFailed("Gateway processing failed");

            payment = paymentRepository.save(payment);

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
            } catch (Exception ignored) {}

            return paymentResultMapper.toResult(payment, null);
        }

        // ----------------------------
        // 4. ALLOCATION + INVOICE UPDATE
        // ----------------------------
        PaymentAllocation allocation =
                allocationService.allocate(invoice, paymentMoney);

        String oldStatus = invoice.getStatus().name();

        invoice.applyPayment(allocation);

        String newStatus = invoice.getStatus().name();

        if (!oldStatus.equals(newStatus)) {
            invoiceAuditService.audit(
                    invoice.getId(),
                    oldStatus,
                    newStatus
            );
        }

        // ----------------------------
        // 5. FINALIZE PAYMENT
        // ----------------------------
        payment.markSuccess();

        invoiceRepository.save(invoice);

        payment = paymentRepository.save(payment);

        transactionRepository.save(
                Transaction.paymentReceived(
                        invoice.getId(),
                        payment.getId(),
                        paymentMoney
                )
        );

        eventPublisher.publish(invoice.pullDomainEvents());

        // ----------------------------
        // 6. SUCCESS AUDIT
        // ----------------------------
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
        } catch (Exception ignored) {}

        // ----------------------------
        // 7. RESPONSE
        // ----------------------------
        return paymentResultMapper.toResult(payment, allocation);
    }
}