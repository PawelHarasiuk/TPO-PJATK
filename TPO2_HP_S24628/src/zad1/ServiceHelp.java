package zad1;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ServiceHelp {


    public static String getCode(String country) {
        Locale[] locales = Locale.getAvailableLocales();
        for (Locale locale : locales) {
            if (country.equalsIgnoreCase(locale.getDisplayCountry(new Locale("eng")))) {
                return locale.getCountry();
            }
        }
        return null;
    }


    public static String getCountryCurrency(String country) {
        Locale locale = new Locale("", Objects.requireNonNull(getCode(country)));
        return String.valueOf(Currency.getInstance(locale));
    }


    public static double getValue(char table, String currency) {
        String url = String.format("https://api.nbp.pl/api/exchangerates/rates/%s/%s/", table, currency);
        String content = "";
        try (
                InputStream inputStream = new URL(url).openConnection().getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))
        ) {
            content = bufferedReader.lines().collect(Collectors.joining());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Pattern pattern = Pattern.compile("\"mid\":\\s*([0-9]+\\.[0-9]+)");
        Matcher matcher = pattern.matcher(content);
        double rate = 0;
        if (matcher.find()) {
            return Double.parseDouble(matcher.group(1));
        }

        return rate;
    }
}
