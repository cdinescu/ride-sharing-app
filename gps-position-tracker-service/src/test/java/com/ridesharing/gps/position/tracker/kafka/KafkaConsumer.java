package com.ridesharing.gps.position.tracker.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.concurrent.CountDownLatch;

@Slf4j
public class KafkaConsumer {
    private final CountDownLatch latch = new CountDownLatch(1);
    private String payload = null;

    @KafkaListener(topics = "${test.topic}", groupId = "group_id")
    public void receive(ConsumerRecord<?, ?> consumerRecord) {
        log.info("received payload='{}'", consumerRecord.toString());
        payload = consumerRecord.toString();
        latch.countDown();
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    public void setPayload(String payLoad) {
        this.payload = payLoad;
    }

    public String getPayload() {
        return payload;
    }
}
