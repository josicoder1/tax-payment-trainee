package com.example.tax_payment.domain.valueobject;

public class PaymentAllocation {

	private final Money penaltyAmount;
	private final Money interestAmount;
	private final Money principalAmount;

	public PaymentAllocation(Money penaltyAmount, Money interestAmount, Money principalAmount) {
		this.penaltyAmount = penaltyAmount;
		this.interestAmount = interestAmount;
		this.principalAmount = principalAmount;
	}

	public Money getPenaltyAmount() {
		return penaltyAmount;
	}

	public Money getInterestAmount() {
		return interestAmount;
	}

	public Money getPrincipalAmount() {
		return principalAmount;
	}
}
