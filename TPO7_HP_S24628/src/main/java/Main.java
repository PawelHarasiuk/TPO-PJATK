import org.springframework.kafka.test.EmbeddedKafkaBroker;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        EmbeddedKafkaBroker broker = new EmbeddedKafkaBroker(1)
                .kafkaPorts(9092)
                .brokerProperty("num.partitions", 1);
        broker.afterPropertiesSet();
        new ChatClient();
        new ChatClient();
    }
}
