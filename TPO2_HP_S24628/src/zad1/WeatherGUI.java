package zad1;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class WeatherGUI extends JFrame {
    private JButton getWeatherButton;
    private JLabel pressureValue;
    private JPanel mainPanel;
    private JLabel humiValue;
    private JPanel webPanel;
    private JTextField cityField;
    private JTextField countryField;
    private JLabel tempValue;
    private JLabel feelsLikeValue;
    private JTextField currencyField;
    private JButton getCurrenceButton;
    private JButton getWebsiteButton;
    private JLabel rateValue;
    private JLabel NBPValue;
    private final JFrame mainFrame;
    //private final Service service;
    private final JFXPanel jfxPanel;

    private JSONWeather jsonWeather;

    public WeatherGUI(Service service) {
        this.mainFrame = this;
        //this.service = service;
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.add(mainPanel);
        jfxPanel = new JFXPanel();
        webPanel.add(jfxPanel);
        mainFrame.setSize(600, 600);
        mainFrame.pack();
        mainFrame.repaint();
        mainFrame.revalidate();

        //do testów
        countryField.setText("Poland");
        cityField.setText("Warsaw");
        //

        getWeatherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jsonWeather = service.getWeatherUser(countryField.getText(), cityField.getText());
                tempValue.setText(jsonWeather.getMain().getTemp() + " C");
                feelsLikeValue.setText(jsonWeather.getMain().getFeelsLike() + " C");
                pressureValue.setText(jsonWeather.getMain().getPressure() + " HPa");
                humiValue.setText(jsonWeather.getMain().getHumidity() + " %");
                mainFrame.repaint();
                mainFrame.pack();
            }
        });

        getCurrenceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //logika, do servisu
                double rate = service.getRateFor(currencyField.getText());
                double rateFor = service.getRateFor(service.getCurrencyInCountry(countryField.getText()));
                rateValue.setText(String.valueOf(rateFor / rate));
                NBPValue.setText(String.valueOf(service.getNBPRate()));
                mainFrame.repaint();
                mainFrame.pack();
            }
        });


        getWebsiteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String url = "https://en.wikipedia.org/wiki/%s";
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        WebView webView = new WebView();
                        WebEngine webEngine = webView.getEngine();
                        webEngine.load(String.format(url, cityField.getText()));
                        jfxPanel.setScene(new Scene(webView));
                        mainFrame.pack();
                    }
                });
                webPanel.removeAll();
                webPanel.add(jfxPanel);
                mainFrame.revalidate();
            }
        });
    }
}
