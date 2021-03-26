package com.ridesharing.gps.position.tracker.eventsender;


import com.ridesharing.gpstrackerservice.GpsPosition;

public interface EventSender {
    void send(GpsPosition newGpsPosition);
}
