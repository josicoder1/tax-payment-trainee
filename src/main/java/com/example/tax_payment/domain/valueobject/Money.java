package com.example.tax_payment.domain.valueobject;

import java.math.BigDecimal;
import java.util.Objects;
import java.math.RoundingMode;

public record Money(BigDecimal amount) {

    public Money {
        Objects.requireNonNull(amount, "amount");

        amount = amount.setScale(2, RoundingMode.HALF_UP);

        if (amount.signum() < 0) {
            throw new IllegalArgumentException("money cannot be negative");
        }
    }

    public static Money zero() {
        return new Money(BigDecimal.ZERO);
    }

    public static Money of(String value) {
        return new Money(new BigDecimal(value));
    }

    public Money add(Money other) {
        return new Money(amount.add(other.amount));
    }

    public Money subtract(Money other) {

        BigDecimal result = amount.subtract(other.amount);

        if (result.signum() < 0) {
            throw new IllegalArgumentException("result cannot be negative");
        }

        return new Money(result);
    }

    public Money min(Money other) {
        return amount.compareTo(other.amount) <= 0 ? this : other;
    }

    public boolean isZero() {
        return amount.signum() == 0;
    }

    public boolean isPositive() {
        return amount.signum() > 0;
    }

    public int compareTo(Money other) {
        return amount.compareTo(other.amount);
    }
}