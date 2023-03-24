import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import zad1.Service;
import zad1.Weather;


public class WeatherGUI extends JFrame {
    private JButton getWeatherButton;
    private JLabel tempValue;
    private JPanel mainPanel;
    private JLabel humiValue;
    private JPanel webPanel;
    private JTextField cityField;
    private JTextField countryField;
    private JFrame mainFrame;
    private final Service service;
    private JFXPanel jfxPanel;

    private Weather weather;

    public WeatherGUI(Service service) {
        this.mainFrame = this;
        this.service = service;
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.add(mainPanel);
        jfxPanel = new JFXPanel();
        //Platform.runLater(this::createJFXContent);
        webPanel.add(jfxPanel);
        this.pack();


        getWeatherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                weather = service.getWeatherUser(countryField.getText(), cityField.getText());
                tempValue.setText(String.valueOf(weather.getMain().getPressure()));
                humiValue.setText(String.valueOf(weather.getMain().getPressure()));
                System.out.println(cityField.getText());
                mainFrame.repaint();
                mainFrame.pack();


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

    public static void main(String[] args) {
        Service service = new Service("Poland");

        SwingUtilities.invokeLater(() -> {
            new WeatherGUI(service);
        });
    }
}
