package com.example.tax_payment.api.validation;

public class TaxpayerTinValidator {

    public static void validate(String tin) {

        if (tin == null || tin.isBlank()) {
            throw new IllegalArgumentException("TIN cannot be empty");
        }

        if (!tin.matches("[0-9A-Z]{8,15}")) {
            throw new IllegalArgumentException("Invalid TIN format");
        }
    }
}