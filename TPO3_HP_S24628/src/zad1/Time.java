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
            String hour1 = "";
            String hour2 = "";
            if (from.contains("T") && to.contains("T")) {
                hour1 += "godz. " + fromDate.getHour() + ":";
                hour2 += "godz. " + toDate.getHour() + ":";

                if ((fromDate.getMinute() + "").length() == 2) {
                    hour1 += fromDate.getMinute();
                } else {
                    hour1 += "0" + fromDate.getMinute() + " ";
                }

                if ((toDate.getMinute() + "").length() == 2) {
                    hour2 += toDate.getMinute();
                } else {
                    hour2 += "0" + toDate.getMinute() + " ";
                }
            }
            String formattedWeek = String.format("%.2f", weeks).replace(",", ".");

            if (formattedWeek.equals("0.00")){
                formattedWeek = "0";
            }

            String result = "Od " + formatDate(date1) + " (" + formatDayOfWeek(date1) + ") " + hour1 + "do " + formatDate(date2) + " (" + formatDayOfWeek(date2) + ") " + hour2 + "\n";
            result += " - mija: " + days + " " + getDay(days) + ", tygodni " + formattedWeek + "\n";

            //nie ma zmiany czasu
            if (from.contains("T") && to.contains("T")) {
                long hours = ChronoUnit.HOURS.between(fromDate, toDate);
                long minutes = ChronoUnit.MINUTES.between(fromDate, toDate) % 60 + hours * 60;
                result += " - godzin: " + hours + ", minut: " + minutes + "\n";
            }

            if (days >= 1) {
                long years = period.getYears();
                long months = period.getMonths();
                int remainingDays = period.getDays();
                if (years > 0 || months > 0 || remainingDays > 0) {
                    result += " - kalendarzowo: ";
                    boolean comm = false;
                    if (years > 0) {
                        result += years + " " + (years == 1 ? "rok" : (years % 10 >= 2 && years % 10 <= 4 && (years % 100 < 10 || years % 100 >= 20) ? "lata" : "lat"));
                        comm = true;
                    }
                    if (months > 0) {
                        if (comm) {
                            result += ", ";
                        }
                        result += months + " " + (months == 1 ? "miesiąc" : (months % 10 >= 2 && months % 10 <= 4 && (months % 100 < 10 || months % 100 >= 20) ? "miesiące" : "miesięcy"));
                        comm = true;
                    }
                    if (remainingDays > 0) {
                        if (comm) {
                            result += ", ";
                        }
                        result += remainingDays + " " + getDay(remainingDays) + " ";
                    }
                }
            }
            return result;
        } catch (DateTimeParseException e) {
            return "*** java.time.format.DateTimeParseException: " + e.getMessage();
        }
    }

    private static String getDay(double remainingDays) {
        return (remainingDays == 1 ? "dzień" : "dni");
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
