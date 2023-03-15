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
import java.util.stream.Collectors;

public class Service {
    private String country;
    //private static final Gson gson = new Gson();
    private static final String MY_KEY = "5bfc9ae25b8bac0ac9349ee446703501";

    public Service(String country) {
        this.country = country;
    }

    public static String getWeather(String city) {
        String urlString = "https://api.openweathermap.org/data/2.5/weather?%s&appid=%s";
        Weather weather = null;
        try {
            InputStream inputStream = new URL(String.format(urlString, getLatAndLon(city), MY_KEY)).openConnection().getInputStream();
            String jSon = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining());
            System.out.println(jSon);

            Gson gson = new Gson();
            weather = gson.fromJson(jSon, Weather.class);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return weather.name;
    }

    private static String getLatAndLon(String city) {
        String urlString = "http://api.openweathermap.org/geo/1.0/direct?q=%s&limit=5&appid=%s";
        Country country = new Country();

        try {
            InputStream inputStream = new URL(String.format(urlString, city, MY_KEY)).openConnection().getInputStream();
            String jSon = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining());
            System.out.println(jSon);

            Gson mygson = new Gson();
            System.out.println(mygson.fromJson(jSon, Country.class).lat);
            //country = mygson.fromJson(jSon, Country.class);

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(country.lat);
        return String.format("lat=%s&lon=%s", country.lat, country.lon);
    }

    public static void main(String[] args) {
        System.out.println(getWeather("London"));
    }











    public Double getRateFor(String currency) {
        return 1.1;
    }

    public Double getNBPRate() {
        return 1.1;
    }
}

