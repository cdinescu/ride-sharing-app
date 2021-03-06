package com.ridesharing.gps.position.tracker.service;

import com.ridesharing.domain.model.ride.gps.position.GpsPosition;

public interface GpsPositionTrackerService {
    void notifyGpsPositionChanged(GpsPosition newGpsPosition);
}
