package zad1;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Tricks {
    public static void main(String[] args) {
        //zrobic petle zeby przechodzila po wartosciach od A do C i szukala value
        double num = findInTable("USD");
        System.out.printf("%.2f%n", num);
    }

    public static double findInTable(String currency) {
        if (currency.equals("PLN")){
            return 1.0;
        }

        for (int i = 'A'; i <= 'C'; i++) {
            double tmp = getValue((char) i, currency);
            if (tmp != 0.0) {
                return tmp;
            }
        }
        return -1;
    }

    public static double getValue(char table, String currency) {
        String url = String.format("https://api.nbp.pl/api/exchangerates/rates/%s/%s/", table, currency);
        String content = "";
        try (
                InputStream inputStream = new URL(url).openConnection().getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))
        ) {
            content = bufferedReader.lines().collect(Collectors.joining());
        } catch (Exception ignored) {

        }

        Pattern pattern = Pattern.compile("\"mid\":\\s*([0-9]+\\.[0-9]+)");
        Matcher matcher = pattern.matcher(content);
        double v = 0;
        if (matcher.find()) {
            return Double.parseDouble(matcher.group(1));
        }

        return v;
    }
}