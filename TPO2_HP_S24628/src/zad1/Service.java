/**
 * @author Harasiuk Paweł S24628
 */

package zad1;


import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Service {
    private static final String MY_KEY = "5bfc9ae25b8bac0ac9349ee446703501";
    private JSONWeather weather;
    private String weatherJson;
    private final Gson gson;

    private String country;
    private String city;
    private String currency;

    public Service(String country) {
        this.country = country;
        this.gson = new Gson();
    }

    public String getWeather(String city) {
        this.city = city;
        this.currency = getCountryCurrency(country);
        String code = getCode(country);
        String urlString = "https://api.openweathermap.org/data/2.5/weather?q=%s,%s&appid=%s&units=metric";
        // TODO zobić sprawdzenie czy city prawidłowe i obsluge bledu
        try (InputStream inputStream = new URL(String.format(urlString, city, code, MY_KEY)).openConnection().getInputStream();
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))
        ) {
            this.weatherJson = bufferedReader.lines().collect(Collectors.joining());
            weather = gson.fromJson(weatherJson, JSONWeather.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return weatherJson;
    }


    public JSONWeather getWeatherGUI(String country, String city) {
        this.country = country;
        this.city = city;
        this.currency = getCountryCurrency(country);

        weather = gson.fromJson(getWeather(city), JSONWeather.class);
        System.out.println(weather.getWeather());
        return weather;
    }


    public Double getRateFor(String currencyCode) {
        String url = String.format("https://api.exchangerate.host/latest?base=%s&symbols=%s", country, currencyCode);
        StringBuilder response = new StringBuilder();

        try (
                InputStream inputStream = new URL(url).openConnection().getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))

        ) {
            URL obj = new URL(url);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
            con.setRequestMethod("GET");

            String inputLine = "";

            while ((inputLine = bufferedReader.readLine()) != null) {
                response.append(inputLine);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        JsonObject jsonObject = gson.fromJson(response.toString(), JsonObject.class);
        return jsonObject.getAsJsonObject("rates").get(currencyCode).getAsDouble();
    }


    public String getCode(String country) {
        Locale[] locales = Locale.getAvailableLocales();
        for (Locale locale : locales) {
            if (country.equalsIgnoreCase(locale.getDisplayCountry(new Locale("eng")))) {
                return locale.getCountry();
            }
        }
        //obsluzyc to
        return null; // Country not found
    }

    public String getCountryCurrency(String country) {
        Locale locale = new Locale("", getCode(country));
        return String.valueOf(Currency.getInstance(locale));
    }

    public double getNBPRate() {
        String c = getCountryCurrency(getCountry());
        if (c.equals("PLN")) {
            return 1.0;
        }

        for (int i = 'A'; i <= 'C'; i++) {
            double tmp = getValue((char) i, c);
            if (tmp != 0.0) {
                return tmp;
            }
        }
        return -1;
    }

    public double getValue(char table, String currency) {
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

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}

