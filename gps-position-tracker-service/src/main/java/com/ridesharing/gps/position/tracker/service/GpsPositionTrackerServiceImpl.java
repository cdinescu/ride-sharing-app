package com.ridesharing.gps.position.tracker.service;

import com.ridesharing.domain.model.ride.gps.position.GpsPosition;
import com.ridesharing.gps.position.tracker.kafka.producer.Producer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class GpsPositionTrackerServiceImpl implements GpsPositionTrackerService {
    private final Producer producer;

    @Override
    public void notifyGpsPositionChanged(GpsPosition newGpsPosition) {
        log.info("Send event for this new GPS position: {}", newGpsPosition);

        if (newGpsPosition == null) {
            log.error("Failed to send update; the new position is NULL!");
            return;
        }

        Message<GpsPosition> gpsPositionMessage = MessageBuilder.withPayload(newGpsPosition)
                .setHeader(KafkaHeaders.TOPIC, "gps-position-topic")
                .build();
        producer.getMySource()
                .output()
                .send(gpsPositionMessage);
    }
}
