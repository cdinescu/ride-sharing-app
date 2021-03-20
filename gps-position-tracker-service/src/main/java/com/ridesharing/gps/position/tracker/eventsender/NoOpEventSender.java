package com.ridesharing.gps.position.tracker.eventsender;

import com.ridesharing.domain.model.ride.gps.position.GpsPosition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile("!prod & !integration")
public class NoOpEventSender implements EventSender {
    @Override
    public void send(GpsPosition newGpsPosition) {
        log.info("Skip sending event for new GPS coordinates ({}) in non production/integration environment", newGpsPosition);
    }
}
