package com.example.tax_payment.domain.valueobject;

import com.example.tax_payment.domain.exception.PaymentValidationException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Money {

    private final BigDecimal amount;
    private final String currency;

    // Factory method for zero in a given currency
    public static Money zero(String currency) {
        return new Money(BigDecimal.ZERO, currency);
    }

    public Money(BigDecimal amount, String currency) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new PaymentValidationException("Amount must be non-negative");
        }
        if (currency == null || currency.trim().isEmpty()) {
            throw new PaymentValidationException("Currency must be provided (3-letter ISO code)");
        }
        String normalizedCurrency = currency.trim().toUpperCase();
        if (normalizedCurrency.length() != 3) {
            throw new PaymentValidationException("Currency must be a 3-letter ISO code, got: " + normalizedCurrency);
        }
        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
        this.currency = normalizedCurrency;
    }

    public Money add(Money other) {
        validateCurrency(other);
        return new Money(this.amount.add(other.amount), this.currency);
    }

    public Money subtract(Money other) {
        validateCurrency(other);
        BigDecimal result = this.amount.subtract(other.amount);
        if (result.compareTo(BigDecimal.ZERO) < 0) {
            throw new PaymentValidationException("Money amount cannot become negative");
        }
        return new Money(result, this.currency);
    }

    public boolean isGreaterThan(Money other) {
        validateCurrency(other);
        return this.amount.compareTo(other.amount) > 0;
    }

    public boolean isLessThan(Money other) {
        validateCurrency(other);
        return this.amount.compareTo(other.amount) < 0;
    }

    public boolean isEqual(Money other) {
        validateCurrency(other);
        return this.amount.compareTo(other.amount) == 0;
    }

    private void validateCurrency(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new PaymentValidationException("Currency mismatch: " + this.currency + " vs " + other.currency);
        }
    }

    public BigDecimal getAmount() { return amount; }
    public String getCurrency() { return currency; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return Objects.equals(amount, money.amount) && Objects.equals(currency, money.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }

    @Override
    public String toString() {
        return amount + " " + currency;
    }
}