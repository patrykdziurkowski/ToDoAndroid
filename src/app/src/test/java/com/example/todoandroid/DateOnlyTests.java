package com.example.todoandroid;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Date;
import java.util.Optional;

public class DateOnlyTests {
    @Test
    public void defaultConstructorWithNoParams_returnsCurrentDate() {
        DateOnly date = new DateOnly();
        Date currentDate = new Date();
        String currentDateString = String.format("%s-%s-%s",
                currentDate.getYear() + Constants.DATE_YEAR_OFFSET,
                currentDate.getMonth() + 1,
                currentDate.getDate());
        assertEquals(currentDateString, date.toString());
    }

    @Test
    public void before_returnsTrue_whenFirstDateBeforeSecond() {
        DateOnly date = new DateOnly(2020, 6, 23);
        DateOnly date2 = new DateOnly(2020, 6, 24);

        assertTrue(date.before(date2));
    }

    @Test
    public void before_returnsFalse_whenFirstDateAfterSecond() {
        DateOnly date = new DateOnly(2020, 6, 23);
        DateOnly date2 = new DateOnly(2020, 6, 20);

        assertFalse(date.before(date2));
    }

    @Test
    public void before_returnsFalse_whenFirstDateEqualsSecond() {
        DateOnly date = new DateOnly(2020, 6, 23);
        DateOnly date2 = new DateOnly(2020, 6, 23);

        assertFalse(date.before(date2));
    }

    @Test
    public void equals_returnsTrue_whenComparingToItself() {
        DateOnly date = new DateOnly(2020, 6, 23);
        assertTrue(date.equals(date));
    }

    @Test
    public void equals_returnsFalse_whenGivenObjectOfDifferentType() {
        DateOnly date = new DateOnly(2020, 6, 23);
        Date date2 = new Date(120, 5, 23);
        assertFalse(date.equals(date2));
    }

    @Test
    public void equals_returnsTrue_whenDatesMatch() {
        DateOnly date = new DateOnly(2020, 6, 23);
        DateOnly date2 = new DateOnly(2020, 6, 23);
        assertTrue(date.equals(date2));
    }

    @Test
    public void equals_returnsFalse_whenDatesDontMatch() {
        DateOnly date = new DateOnly(2020, 6, 23);
        DateOnly date2 = new DateOnly(2020, 6, 24);
        assertFalse(date.equals(date2));
    }

    @Test
    public void compareTo_returnsMinusOne_whenFirstDateIsEarlier() {
        DateOnly date = new DateOnly(2020, 6, 23);
        DateOnly date2 = new DateOnly(2022, 4, 15);

        assertEquals(-1, date.compareTo(date2));
    }

    @Test
    public void compareTo_returnsZero_whenDatesMatch() {
        DateOnly date = new DateOnly(2020, 6, 23);
        DateOnly date2 = new DateOnly(2020, 6, 23);

        assertEquals(0, date.compareTo(date2));
    }

    @Test
    public void compareTo_returnsOne_whenFirstDateIsLater() {
        DateOnly date = new DateOnly(2022, 6, 23);
        DateOnly date2 = new DateOnly(2020, 4, 15);

        assertEquals(1, date.compareTo(date2));
    }

    @Test
    public void parse_returnsDateOnly_whenDateParsable() {
        Optional<DateOnly> result = DateOnly.parse("2024-6-24");
        assertTrue(result.isPresent());

        DateOnly date = result.get();
        assertEquals(2024, date.getYear());
        assertEquals(6, date.getMonth());
        assertEquals(24, date.getDayOfMonth());
    }

    @Test
    public void parse_returnsEmpty_whenDateNotParsable() {
        Optional<DateOnly> result = DateOnly.parse("2024-July-24");
        assertFalse(result.isPresent());
    }

    @Test
    public void toString_returnsCorrectString_whenGivenDate() {
        DateOnly date = new DateOnly(2024, 11, 23);
        assertEquals("2024-11-23", date.toString());
    }

    @Test
    public void toString_returnsCorrectString_whenGivenDateWithSingleDigitMonth() {
        DateOnly date = new DateOnly(2024, 6, 12);
        assertEquals("2024-6-12", date.toString());
    }

    @Test
    public void toString_returnsCorrectString_whenGivenDateWithSingleDigitDayOfMonth() {
        DateOnly date = new DateOnly(2024, 11, 9);
        assertEquals("2024-11-9", date.toString());
    }

    @Test
    public void toString_throws_whenGivenDateWithMonthAndDayEqualToZero() {
        assertThrows(IllegalArgumentException.class, () -> {
            DateOnly date = new DateOnly(2024, 0, 0);
        });
    }
}
