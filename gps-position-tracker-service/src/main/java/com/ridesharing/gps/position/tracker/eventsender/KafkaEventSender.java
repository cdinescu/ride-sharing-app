package com.ridesharing.gps.position.tracker.eventsender;

import com.ridesharing.gps.position.tracker.kafka.producer.Producer;
import com.ridesharing.gpstrackerservice.GpsPosition;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile("!test")
@AllArgsConstructor
public class KafkaEventSender implements EventSender {
    private final Producer producer;

    @Override
    public void send(GpsPosition newGpsPosition) {
        log.info("Sending {} to KAFKA", newGpsPosition);

        Message<GpsPosition> gpsPositionMessage = MessageBuilder.withPayload(newGpsPosition)
                .setHeader(KafkaHeaders.TOPIC, "gps-position-topic")
                .build();
        producer.getMySource()
                .output()
                .send(gpsPositionMessage);
    }
}
