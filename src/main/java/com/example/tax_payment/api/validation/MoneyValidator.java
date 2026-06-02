package com.example.tax_payment.api.validation;

import java.math.BigDecimal;

public class MoneyValidator {

    public static void validatePositive(BigDecimal amount) {

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
    }
}