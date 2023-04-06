package zad1;

import java.time.*;
import java.time.format.*;
import java.time.temporal.*;
import java.util.*;


public class Time {

    public static String passed(String from, String to) {
        try {
            LocalDateTime fromDate = parseDateTime(from);
            LocalDateTime toDate = parseDateTime(to);
            LocalDate date1 = fromDate.toLocalDate();
            LocalDate date2 = toDate.toLocalDate();
            Period period = Period.between(date1, date2);
            long days = ChronoUnit.DAYS.between(date1, date2);
            double weeks = (double) days / 7;
            String result = "Od " + formatDate(date1) + " (" + formatDayOfWeek(date1) + ") do " + formatDate(date2) + " (" + formatDayOfWeek(date2) + ")\n";
            result += "- mija: " + days + " dni, tygodni " + String.format("%.2f", weeks) + "\n";
            if (days >= 1) {
                long years = period.getYears();
                long months = period.getMonths();
                int remainingDays = period.getDays();
                if (years > 0 || months > 0 || remainingDays > 0) {
                    result += "- kalendarzowo: ";
                    if (years > 0) {
                        result += years + " " + (years == 1 ? "rok" : (years % 10 >= 2 && years % 10 <= 4 && (years % 100 < 10 || years % 100 >= 20) ? "lata" : "lat")) + ", ";
                    }
                    if (months > 0) {
                        result += months + " " +  (months == 1 ? "miesiąc" : (months % 10 >= 2 && months % 10 <= 4 && (months % 100 < 10 || months % 100 >= 20) ? "miesiące" : "miesięcy")) + ", ";
                    }
                    if (remainingDays > 0) {
                        result += remainingDays + " " + (remainingDays == 1 ? "dzień" : "dni") + " ";
                    }
                }
            }
            if (fromDate.getHour() != toDate.getHour() || fromDate.getMinute() != toDate.getMinute()) {
                long hours = ChronoUnit.HOURS.between(fromDate, toDate);
                long minutes = ChronoUnit.MINUTES.between(fromDate, toDate) % 60;
                result += "- godzin: " + hours + ", minut: " + minutes;
            }
            return result;
        } catch (DateTimeParseException e) {
            return "*** java.time.format.DateTimeParseException: " + e.getMessage();
        }
    }

    private static LocalDateTime parseDateTime(String dateTime) {
        if (dateTime.length() == 10) {
            return LocalDate.parse(dateTime).atStartOfDay();
        } else {
            return LocalDateTime.parse(dateTime);
        }
    }

    private static String formatDate(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("d MMMM yyyy", new Locale("pl")));
    }

    private static String formatDayOfWeek(LocalDate date) {
        return date.getDayOfWeek().getDisplayName(TextStyle.FULL_STANDALONE, new Locale("pl"));
    }
}
