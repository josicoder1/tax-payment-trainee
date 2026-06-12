package com.example.tax_payment.application.port.outbound;

import java.time.YearMonth;

public interface InvoiceNumberGeneratorPort {

    String nextInvoiceNumber(YearMonth taxPeriod);
}
