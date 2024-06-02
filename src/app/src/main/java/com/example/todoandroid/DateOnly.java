package com.example.todoandroid;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

public class DateOnly implements Comparable<DateOnly> {
    private Date date;

    public DateOnly(int year, int month, int dayOfMonth) {
        if (year <= 0 || month <= 0 || dayOfMonth <= 0) {
            throw new IllegalArgumentException("All date arguments must be greater than zero");
        }

        this.date = new Date(
                year - Constants.DATE_YEAR_OFFSET,
                month - 1,
                dayOfMonth);
    }

    public DateOnly(Date date) {
        this.date = date;
    }

    public DateOnly() {
        this.date = new Date();
    }

    public boolean before(DateOnly date2) {
        if (date2 == null) throw new IllegalArgumentException("Provided date cannot be null");

        if (getYear() > date2.getYear()) return false;
        if (getYear() < date2.getYear()) return true;

        if (getMonth() > date2.getMonth()) return false;
        if (getMonth() < date2.getMonth()) return true;

        return getDayOfMonth() < date2.getDayOfMonth();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof DateOnly)) return false;

        DateOnly date2 = (DateOnly) o;
        return getYear() == date2.getYear()
                && getMonth() == date2.getMonth()
                && getDayOfMonth() == date2.getDayOfMonth();
    }

    public int compareTo(DateOnly date2) {
        if (this.before(date2)) {
            return -1;
        } else if (this.equals(date2)) {
            return 0;
        } else {
            return 1;
        }
    }

    public Date toDate() { return date; }

    @Override
    public String toString() {
        return String.format("%s-%s-%s",
                date.getYear() + Constants.DATE_YEAR_OFFSET,
                date.getMonth() + 1,
                date.getDate());
    }

    public static Optional<DateOnly> parse(String date) {
        try {
            Date parsedDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
            DateOnly dateOnly = new DateOnly(parsedDate);
            return Optional.of(dateOnly);
        } catch (ParseException e) {
            return Optional.empty();
        }
    }

    public int getYear() { return date.getYear() + Constants.DATE_YEAR_OFFSET; }
    public int getMonth() { return date.getMonth() + 1; }
    public int getDayOfMonth() { return date.getDate(); }
}
