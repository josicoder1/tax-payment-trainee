package com.example.tax_payment.domain.valueobject;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public record TaxPeriod(
        LocalDate start,
        LocalDate end,
        PeriodFrequency frequency
) {

    private static final DateTimeFormatter MONTH_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM");

    private static final DateTimeFormatter DAY_FORMAT =
            DateTimeFormatter.ISO_LOCAL_DATE;

    public TaxPeriod {

        Objects.requireNonNull(start, "start cannot be null");
        Objects.requireNonNull(end, "end cannot be null");
        Objects.requireNonNull(frequency, "frequency cannot be null");

        if (end.isBefore(start)) {
            throw new IllegalArgumentException(
                    "end date cannot be before start date"
            );
        }
    }

    public static TaxPeriod monthly(YearMonth yearMonth) {

        return new TaxPeriod(
                yearMonth.atDay(1),
                yearMonth.atEndOfMonth(),
                PeriodFrequency.MONTHLY
        );
    }

    public static TaxPeriod daily(LocalDate date) {

        return new TaxPeriod(
                date,
                date,
                PeriodFrequency.DAILY
        );
    }

    public static TaxPeriod quarterly(int year, int quarter) {

        if (quarter < 1 || quarter > 4) {
            throw new IllegalArgumentException("quarter must be between 1 and 4");
        }

        LocalDate start = LocalDate.of(year, (quarter - 1) * 3 + 1, 1);
        LocalDate end = start.plusMonths(3).minusDays(1);

        return new TaxPeriod(start, end, PeriodFrequency.QUARTERLY);
    }

    public static TaxPeriod annual(int year) {

        return new TaxPeriod(
                LocalDate.of(year, 1, 1),
                LocalDate.of(year, 12, 31),
                PeriodFrequency.ANNUAL
        );
    }

    public boolean contains(LocalDate date) {

        return (date.isEqual(start) || date.isAfter(start)) &&
                (date.isEqual(end) || date.isBefore(end));
    }

    public boolean overlaps(TaxPeriod other) {

        return !this.end.isBefore(other.start) &&
                !other.end.isBefore(this.start);
    }

    public long days() {

        return java.time.temporal.ChronoUnit.DAYS.between(start, end) + 1;
    }

    public boolean isSingleDay() {

        return start.equals(end);
    }

    public String label() {

        return switch (frequency) {

            case DAILY ->
                    start.format(DAY_FORMAT);

            case MONTHLY ->
                    YearMonth.from(start).format(MONTH_FORMAT);

            case QUARTERLY -> {
                int quarter = (start.getMonthValue() - 1) / 3 + 1;
                yield start.getYear() + "-Q" + quarter;
            }

            case ANNUAL ->
                    String.valueOf(start.getYear());
        };
    }
}
