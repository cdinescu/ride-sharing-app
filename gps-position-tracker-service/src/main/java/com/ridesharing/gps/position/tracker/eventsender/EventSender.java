package com.ridesharing.gps.position.tracker.eventsender;

import com.ridesharing.domain.model.ride.gps.position.GpsPosition;

public interface EventSender {
    void send(GpsPosition newGpsPosition);
}
