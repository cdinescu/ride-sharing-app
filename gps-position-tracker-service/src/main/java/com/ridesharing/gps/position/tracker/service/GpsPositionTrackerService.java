package com.ridesharing.gps.position.tracker.service;

import com.ridesharing.gpstrackerservice.GpsPosition;

public interface GpsPositionTrackerService {
    void notifyGpsPositionChanged(GpsPosition newGpsPosition);
}
