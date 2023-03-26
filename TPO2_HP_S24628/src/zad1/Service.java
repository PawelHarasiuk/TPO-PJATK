/**
 *
 *  @author Harasiuk Paweł S24628
 *
 */

package zad1;


import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.stream.Collectors;

public class Service {
    private static final String MY_KEY = "5bfc9ae25b8bac0ac9349ee446703501";
    private static final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather?q=%s,%s&appid=%s&units=metric";
    private static final String EXCHANGE_URL = "https://api.exchangerate.host/convert?from=%s&to=%s";
    private final Gson gson;

    private String country;
    private String city;
    private String currency;

    public Service(String country) {
        this.country = country;
        this.currency = ServiceHelp.getCountryCurrency(country);

        this.gson = new Gson();
    }

    public String getWeather(String city) {
        String code = ServiceHelp.getCode(country);
        String weatherJson = null;

        try (InputStream inputStream = new URL(String.format(WEATHER_URL, city, code, MY_KEY)).openConnection().getInputStream();
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))
        ) {
            weatherJson = bufferedReader.lines().collect(Collectors.joining());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return weatherJson;
    }


    public Double getRateFor(String currencyCode) {
        String url = String.format(EXCHANGE_URL, currencyCode, ServiceHelp.getCountryCurrency(country));
        StringBuilder response = new StringBuilder();

        try (
                InputStream inputStream = new URL(url).openConnection().getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))
        ) {
            String inputLine;

            while ((inputLine = bufferedReader.readLine()) != null) {
                response.append(inputLine);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        JsonObject jsonObject = gson.fromJson(response.toString(), JsonObject.class);
        return jsonObject.getAsJsonObject("info").get("rate").getAsDouble();
    }


    public double getNBPRate() {
        String c = ServiceHelp.getCountryCurrency(getCountry());
        if (c.equals("PLN")) {
            return 1.0;
        }

        for (int i = 'A'; i <= 'C'; i++) {
            double tmp = ServiceHelp.getValue((char) i, c);
            if (tmp != 0.0) {
                return tmp;
            }
        }
        return -1.0;
    }


    public JSONWeather getMyWeather() {
        String json = getWeather(getCity());
        if (json.equals("")) {
            return null;
        }
        return gson.fromJson(json, JSONWeather.class);
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

