package com.example.tax_payment.application.mapper;

import com.example.tax_payment.application.result.AllocationResult;
import com.example.tax_payment.application.result.PaymentResult;
import com.example.tax_payment.domain.model.Payment;
import com.example.tax_payment.domain.service.PaymentAllocation;
import org.springframework.stereotype.Component;

@Component
public class PaymentResultMapper {

    public PaymentResult toResult(
            Payment payment,
            PaymentAllocation allocation
    ) {

        AllocationResult allocationResult = allocation == null
                ? null
                : new AllocationResult(
                allocation.penaltyAllocated().getAmount(),
                allocation.interestAllocated().getAmount(),
                allocation.principalAllocated().getAmount(),
                allocation.penaltyAllocated().getCurrency()
        );

        return new PaymentResult(
                payment.getId(),
                payment.getReferenceNumber(),
                payment.getStatus().name(),
                payment.getFailureReason(),
                "Payment processed successfully",
                payment.getCreatedAt(),
                allocationResult
        );
    }
}