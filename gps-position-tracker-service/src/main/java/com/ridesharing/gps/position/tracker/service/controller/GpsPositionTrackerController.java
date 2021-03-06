package com.ridesharing.gps.position.tracker.service.controller;

import com.ridesharing.domain.model.ride.gps.position.GpsPosition;
import com.ridesharing.gps.position.tracker.service.GpsPositionTrackerService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gps-receiver")
public class GpsPositionTrackerController {

    private final GpsPositionTrackerService gpsPositionTrackerService;

    public GpsPositionTrackerController(GpsPositionTrackerService gpsPositionTrackerService) {
        this.gpsPositionTrackerService = gpsPositionTrackerService;
    }

    @PostMapping
    public void updateGps(@RequestBody GpsPosition position) {
        gpsPositionTrackerService.notifyGpsPositionChanged(position);
    }
}
