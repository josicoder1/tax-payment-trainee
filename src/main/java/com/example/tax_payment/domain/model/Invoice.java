package com.example.tax_payment.domain.model;

import com.example.tax_payment.domain.valueobject.Money;

import java.util.UUID;

public class Invoice {
    private final String id;
    private final Money principalAmount;
    private final Money interestAmount;
    private final Money penaltyAmount;
    private Money totalPaidPrincipal;
    private Money totalPaidInterest;
    private Money totalPaidPenalty;

    public Invoice(String id, Money principalAmount, Money interestAmount, Money penaltyAmount) {
        this.id = id;
        this.principalAmount = principalAmount;
        this.interestAmount = interestAmount;
        this.penaltyAmount = penaltyAmount;
        String currency = principalAmount.getCurrency();
        this.totalPaidPrincipal = Money.zero(currency);
        this.totalPaidInterest = Money.zero(currency);
        this.totalPaidPenalty = Money.zero(currency);
    }

    public String getCurrency() {
        return principalAmount.getCurrency();
    }

    public Money getPrincipalAmount() { return principalAmount; }
    public Money getInterestAmount() { return interestAmount; }
    public Money getPenaltyAmount() { return penaltyAmount; }

    public Money getTotalPaidPrincipal() { return totalPaidPrincipal; }
    public Money getTotalPaidInterest() { return totalPaidInterest; }
    public Money getTotalPaidPenalty() { return totalPaidPenalty; }

    public Money getOutstandingPrincipal() {
        return principalAmount.subtract(totalPaidPrincipal);
    }

    public Money getOutstandingInterest() {
        return interestAmount.subtract(totalPaidInterest);
    }

    public Money getOutstandingPenalty() {
        return penaltyAmount.subtract(totalPaidPenalty);
    }

    // Called by a repository or application service after allocation
    public void addPaymentAllocation(PaymentAllocation allocation) {
        this.totalPaidPenalty = this.totalPaidPenalty.add(allocation.penaltyAllocated());
        this.totalPaidInterest = this.totalPaidInterest.add(allocation.interestAllocated());
        this.totalPaidPrincipal = this.totalPaidPrincipal.add(allocation.principalAllocated());
    }
}