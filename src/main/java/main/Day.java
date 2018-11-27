package main;

import java.time.DayOfWeek;

public enum Day {
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY,
    ;

    public static Day fromDayOfWeek(DayOfWeek dayOfWeek) {
        //
        return Day.values()[dayOfWeek.getValue() - 1];
    }

    public static Day dayFromName(String dayName) {
        for (Day day : Day.values()) {
            if (day.name().equalsIgnoreCase(dayName)) return day;
        }
        return null;
    }
}
