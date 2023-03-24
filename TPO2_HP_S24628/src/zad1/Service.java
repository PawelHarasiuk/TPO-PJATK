/**
 * @author Harasiuk Paweł S24628
 */

package zad1;


import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Locale;
import java.util.stream.Collectors;

public class Service {
    //private static final Gson gson = new Gson();
    private static final String MY_KEY = "5bfc9ae25b8bac0ac9349ee446703501";
    //private static String urlString = "https://api.openweathermap.org/data/2.5/weather?%s&appid=%s";
    private Weather weather;
    private String weatherJson;

    private String country;
    private String city;
    private String currency;

    public Service(String country) {
        this.country = country;
    }

    //do maina do zadnianie
    public String getWeather(String city) {
        this.city = city;
        String code = getCode(country);
        String urlString = "https://api.openweathermap.org/data/2.5/weather?q=%s,%s&appid=%s";
        // TODO zobić sprawdzenie czy city prawidłowe i obsluge bledu
        try (InputStream inputStream = new URL(String.format(urlString, city, code, MY_KEY)).openConnection().getInputStream();
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        ) {
            this.weatherJson = bufferedReader.lines().collect(Collectors.joining());

            Gson gson = new Gson();
            weather = gson.fromJson(weatherJson, Weather.class);
            System.out.println(weather.main.pressure);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return weatherJson;
    }


    //potrzebuje ten obiekt potem jakos polaczyc z tym z main
    public Weather getWeatherUser(String country, String city) {
        String code = getCode(country);
        String urlString = "https://api.openweathermap.org/data/2.5/weather?q=%s,%s&appid=%s";
        // TODO zobić sprawdzenie czy city prawidłowe i obsluge bledu
        try (InputStream inputStream = new URL(String.format(urlString, city, code, MY_KEY)).openConnection().getInputStream();
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        ) {
            this.weatherJson = bufferedReader.lines().collect(Collectors.joining());

            Gson gson = new Gson();
            weather = gson.fromJson(weatherJson, Weather.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return weather;
    }

/*
    public Weather getRequest(String city) {
        try (InputStream inputStream = new URL(String.format(urlString, MY_KEY)).openConnection().getInputStream();
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        ) {
            this.weatherJson = bufferedReader.lines().collect(Collectors.joining());

            Gson gson = new Gson();
            weather = gson.fromJson(weatherJson, Weather.class);
            System.out.println(weather.main.pressure);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return weather;
    }
*/


    public Double getRateFor(String currency) {
        return 1.1;
    }

    public Double getNBPRate() {
        return 1.1;
    }


    private String getCode(String country) {
        Locale locale = new Locale("", country);
        return locale.getCountry().toLowerCase();
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
}

