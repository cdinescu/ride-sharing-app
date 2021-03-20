package com.ridesharing.gps.position.tracker.service;

import com.ridesharing.domain.model.ride.gps.position.GpsPosition;
import com.ridesharing.gps.position.tracker.eventsender.EventSender;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class GpsPositionTrackerServiceImpl implements GpsPositionTrackerService {
    private final EventSender eventSender;

    @Override
    public void notifyGpsPositionChanged(GpsPosition newGpsPosition) {
        log.info("Send event for this new GPS position: {}", newGpsPosition);

        if (newGpsPosition == null) {
            log.error("Failed to send update; the new position is NULL!");
            return;
        }

        eventSender.send(newGpsPosition);
    }
}
