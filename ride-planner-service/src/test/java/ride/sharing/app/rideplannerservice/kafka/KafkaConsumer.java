package ride.sharing.app.rideplannerservice.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Slf4j
@Component
@EnableKafka
public class KafkaConsumer {
    private CountDownLatch latch = new CountDownLatch(1);
    private String payload = null;

    public KafkaConsumer() {
        log.info("Boo! KafkaConsumer");
    }

    @KafkaListener(topics = "ride-planning-topic", groupId = "group_id")
    public void receive(ConsumerRecord<?, ?> consumerRecord) {
        log.info("received payload='{}'", consumerRecord.toString());
        payload = consumerRecord.toString();
        latch.countDown();
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    public String getPayload() {
        return payload;
    }
}
