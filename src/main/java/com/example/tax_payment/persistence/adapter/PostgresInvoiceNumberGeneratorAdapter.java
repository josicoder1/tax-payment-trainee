package com.example.tax_payment.persistence.adapter;

import com.example.tax_payment.application.port.outbound.InvoiceNumberGeneratorPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.YearMonth;

@Component
public class PostgresInvoiceNumberGeneratorAdapter implements InvoiceNumberGeneratorPort {

    private final JdbcTemplate jdbcTemplate;

    public PostgresInvoiceNumberGeneratorAdapter(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public String nextInvoiceNumber(YearMonth taxPeriod) {
        Long sequence = jdbcTemplate.queryForObject(
                "SELECT nextval('invoice_number_seq')",
                Long.class
        );

        return String.format(
                "INV-%04d%02d-%06d",
                taxPeriod.getYear(),
                taxPeriod.getMonthValue(),
                sequence
        );
    }
}
