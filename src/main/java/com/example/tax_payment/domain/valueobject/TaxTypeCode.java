package com.example.tax_payment.domain.valueobject;

import java.util.Objects;

public record TaxTypeCode(String value) {

    public TaxTypeCode {

        Objects.requireNonNull(value, "tax type code cannot be null");

        if (value.isBlank()) {
            throw new IllegalArgumentException(
                    "tax type code cannot be blank"
            );
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
