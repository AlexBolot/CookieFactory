package main;

import org.junit.Test;

import java.time.DayOfWeek;

import static junit.framework.TestCase.assertEquals;

public class DayTest {
    @Test
    public void shouldReturnTheMatchingDay() {
        assertEquals(Day.TUESDAY, Day.fromDayOfWeek(DayOfWeek.TUESDAY));
        assertEquals(Day.MONDAY, Day.fromDayOfWeek(DayOfWeek.MONDAY));
        assertEquals(Day.SUNDAY, Day.fromDayOfWeek(DayOfWeek.SUNDAY));
    }
}
