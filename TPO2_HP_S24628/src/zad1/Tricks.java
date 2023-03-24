package zad1;

import java.util.Currency;
import java.util.Locale;

public class Tricks {
    public static void main(String[] args) {
        for (Locale availableLocale : Locale.getAvailableLocales()) {
            System.out.println(availableLocale.getDisplayCountry(new Locale("pl")));

            try {
                System.out.println(Currency.getInstance(availableLocale));
            } catch (Exception e){}
        }
    }
}