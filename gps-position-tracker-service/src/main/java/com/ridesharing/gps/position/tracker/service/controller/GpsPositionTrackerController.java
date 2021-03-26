package com.ridesharing.gps.position.tracker.service.controller;

import com.ridesharing.gps.position.tracker.service.GpsPositionTrackerService;
import com.ridesharing.gpstrackerservice.GpsPosition;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gps-receiver")
@AllArgsConstructor
public class GpsPositionTrackerController {

    private final GpsPositionTrackerService gpsPositionTrackerService;

    @PostMapping
    public void updateGps(@RequestBody GpsPosition position) {
        gpsPositionTrackerService.notifyGpsPositionChanged(position);
    }
}
