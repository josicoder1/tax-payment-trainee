package com.example.tax_payment.api.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CreateInvoiceRequestValidationTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void tearDownValidator() {
        if (validatorFactory != null) {
            validatorFactory.close();
        }
    }

    @Test
    void nullTaxYearIsRejected() {
        CreateInvoiceRequest request = new CreateInvoiceRequest(
                "123456789",
                "VAT",
                null,
                6,
                BigDecimal.TEN,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                "USD"
        );

        Set<ConstraintViolation<CreateInvoiceRequest>> violations = validator.validate(request);

        assertTrue(
                violations.stream().anyMatch(violation -> violation.getPropertyPath().toString().equals("taxYear")),
                "Expected taxYear to be marked as invalid"
        );
    }

    @Test
    void blankTaxpayerTinIsRejected() {
        CreateInvoiceRequest request = new CreateInvoiceRequest(
                "",
                "VAT",
                2026,
                6,
                BigDecimal.TEN,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                "USD"
        );

        Set<ConstraintViolation<CreateInvoiceRequest>> violations = validator.validate(request);

        assertTrue(
                violations.stream().anyMatch(violation -> violation.getPropertyPath().toString().equals("taxpayerTin")),
                "Expected taxpayerTin to be marked as invalid"
        );
    }
}
