package zad1;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.swing.*;
import java.awt.*;


public class WeatherGUI extends JFrame {
    private JButton getWeatherButton;
    private JLabel pressureValue;
    private JPanel mainPanel;
    private JLabel humiValue;
    private JPanel webPanel;
    private JTextField cityField;
    private JTextField countryField;
    private JLabel tempValue;
    private JLabel skyValue;
    private JTextField currencyField;
    private JButton getCurrencyButton;
    private JButton getWebsiteButton;
    private JLabel rateValue;
    private JLabel NBPValue;
    private final JFrame mainFrame;
    private final JFXPanel jfxPanel;
    private final String WEBSITE_URL = "https://en.wikipedia.org/wiki/%s";

    private JSONWeather weather;

    public WeatherGUI(Service service) {
        this.mainFrame = this;
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.add(mainPanel);
        this.setPreferredSize(new Dimension(600, 600));
        this.setVisible(true);
        jfxPanel = new JFXPanel();
        webPanel.add(jfxPanel);
        mainFrame.pack();

        countryField.setText("Poland");
        cityField.setText("Warsaw");
        currencyField.setText("PLN");

        tempValue.setText("0");
        skyValue.setText("");
        humiValue.setText("0");
        pressureValue.setText("0");
        rateValue.setText("0");
        NBPValue.setText("0");

        getWeatherButton.addActionListener(e -> {
            service.setCurrency(ServiceHelp.getCountryCurrency(countryField.getText()));
            service.setCountry(countryField.getText());
            service.setCity(cityField.getText());
            service.getWeather(service.getCity());

            weather = service.getMyWeather();

            if (weather != null) {
                tempValue.setText(weather.getMain().getTemp() + " C");
                skyValue.setText(weather.getWeather());
                pressureValue.setText(weather.getMain().getPressure() + " HPa");
                humiValue.setText(weather.getMain().getHumidity() + " %");
            }
        });

        getCurrencyButton.addActionListener(e -> {
            service.setCountry(countryField.getText());
            service.setCurrency(ServiceHelp.getCountryCurrency(countryField.getText()));

            double rateFor = service.getRateFor(currencyField.getText());
            double rate = service.getNBPRate();

            rateValue.setText(String.valueOf(rateFor));
            NBPValue.setText(String.valueOf(rate));
        });


        getWebsiteButton.addActionListener(e -> {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    WebView webView = new WebView();
                    WebEngine webEngine = webView.getEngine();
                    webEngine.load(String.format(WEBSITE_URL, cityField.getText()));
                    jfxPanel.setScene(new Scene(webView));
                }
            });
            webPanel.removeAll();
            webPanel.add(jfxPanel);
            mainFrame.revalidate();
            mainFrame.repaint();
            mainFrame.pack();
        });
    }

}
