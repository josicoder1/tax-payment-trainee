package com.example.tax_payment.domain.exception;

public class InvalidMoneyOperationException
        extends DomainException {

    public InvalidMoneyOperationException(String message) {
        super(message);
    }
}