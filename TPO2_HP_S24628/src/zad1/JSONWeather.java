package zad1;

import java.util.List;

public class JSONWeather {
    MainData main;
    List<MainWeather> weather;

    public MainData getMain() {
        return main;
    }

    public String getWeather() {
        return weather.get(0).getDescription();
    }
}
