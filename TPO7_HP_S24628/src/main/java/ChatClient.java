import org.apache.kafka.clients.producer.ProducerRecord;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class ChatClient extends JFrame {
    private JTextField message;
    private JButton sendButton;
    private JTextArea messages;
    private JButton loginButton;
    private JTextField loginField;
    private JPanel mainPanel;
    private String id;
    private MessageConsumer messageConsumer;
    private LocalDate currentDate;
    private boolean isFirstMessageOfDay;
    private boolean isLoggedIn;

    private static final Map<String, ChatClient> loggedInClients = new HashMap<>();

    public ChatClient() {
        isLoggedIn = false;
        currentDate = LocalDate.now();
        isFirstMessageOfDay = true;

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isLoggedIn) {
                    id = loginField.getText().trim();
                    if (!id.isEmpty() && !loggedInClients.containsKey(id)) {
                        createNewClient();
                        setTitle(id);
                        loginField.setEnabled(false);
                        loginButton.setEnabled(false);
                        isLoggedIn = true;
                    } else {
                        JOptionPane.showMessageDialog(ChatClient.this, "Please enter a valid ID.");
                    }
                }
            }
        });

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isLoggedIn) {
                    String text = message.getText();
                    if (!text.isEmpty()) {
                        MessageProducer.send(new ProducerRecord<>("chat", id + ": " + text));
                    }
                } else {
                    JOptionPane.showMessageDialog(ChatClient.this, "You are not logged in. Please log in first.");
                }
            }
        });

        this.add(mainPanel);
        mainPanel.setPreferredSize(new Dimension(600, 400));
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Chat Client");
        this.pack();
    }

    private void createNewClient() {
        loggedInClients.put(id, this);
        messageConsumer = new MessageConsumer("chat", id);
        startMessageConsumerThread();
    }

    private void appendMessage(String value) {
        LocalDateTime now = LocalDateTime.now();
        LocalDate newDate = now.toLocalDate();

        if (!newDate.equals(currentDate)) {
            currentDate = newDate;
            isFirstMessageOfDay = true;
        }

        if (isFirstMessageOfDay) {
            messages.append(getCurrentDate(newDate) + System.lineSeparator());
            isFirstMessageOfDay = false;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String formattedTime = now.format(formatter);

        messages.append(formattedTime + " " + value + System.lineSeparator());
    }

    private static String getCurrentDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(formatter);
    }

    private void startMessageConsumerThread() {
        new Thread(() -> {
            while (true) {
                messageConsumer.getConsumer().poll(Duration.of(1, ChronoUnit.SECONDS)).iterator().forEachRemaining(
                        c -> {
                            appendMessage(c.value());
                        }
                );
            }
        }).start();
    }
}