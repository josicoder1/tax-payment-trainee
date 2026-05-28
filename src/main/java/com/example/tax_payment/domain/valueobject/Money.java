package com.example.tax_payment.domain.valueobject;

import com.example.tax_payment.domain.exception.PaymentValidationException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public record Money(BigDecimal amount, String currency) {

    public Money {

        Objects.requireNonNull(amount, "amount");

        if (currency == null || currency.trim().isEmpty()) {
            throw new PaymentValidationException(
                    "Currency must be provided"
            );
        }

        currency = currency.trim().toUpperCase();

        if (currency.length() != 3) {
            throw new PaymentValidationException(
                    "Currency must be a 3-letter ISO code"
            );
        }

        amount = amount.setScale(2, RoundingMode.HALF_UP);

        if (amount.signum() < 0) {
            throw new IllegalArgumentException(
                    "money cannot be negative"
            );
        }
    }

    public static Money zero(String currency) {
        return new Money(BigDecimal.ZERO, currency);
    }

    public static Money of(String value, String currency) {
        return new Money(new BigDecimal(value), currency);
    }

    public Money add(Money other) {
        validateCurrency(other);

        return new Money(
                amount.add(other.amount),
                currency
        );
    }

    public Money subtract(Money other) {

        validateCurrency(other);

        BigDecimal result =
                amount.subtract(other.amount);

        if (result.signum() < 0) {
            throw new IllegalArgumentException(
                    "result cannot be negative"
            );
        }

        return new Money(result, currency);
    }

    public Money min(Money other) {

        validateCurrency(other);

        return amount.compareTo(other.amount) <= 0
                ? this
                : other;
    }

    public boolean isZero() {
        return amount.signum() == 0;
    }

    public boolean isPositive() {
        return amount.signum() > 0;
    }

    public boolean isGreaterThan(Money other) {

        validateCurrency(other);

        return amount.compareTo(other.amount) > 0;
    }

    public boolean isLessThan(Money other) {

        validateCurrency(other);

        return amount.compareTo(other.amount) < 0;
    }

    public boolean isEqual(Money other) {

        validateCurrency(other);

        return amount.compareTo(other.amount) == 0;
    }

    public int compareTo(Money other) {

        validateCurrency(other);

        return amount.compareTo(other.amount);
    }

    private void validateCurrency(Money other) {

        Objects.requireNonNull(other);

        if (!currency.equals(other.currency)) {
            throw new PaymentValidationException(
                    "Currency mismatch: "
                            + currency
                            + " vs "
                            + other.currency
            );
        }
    }

    // Compatibility methods for existing code

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    @Override
    public String toString() {
        return amount + " " + currency;
    }
}