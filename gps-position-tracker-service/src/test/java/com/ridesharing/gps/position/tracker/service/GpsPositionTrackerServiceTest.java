package com.ridesharing.gps.position.tracker.service;

import com.ridesharing.domain.model.ride.gps.position.GpsPosition;
import com.ridesharing.gps.position.tracker.eventsender.EventSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GpsPositionTrackerServiceTest {
    @SpyBean
    private EventSender eventSender;

    private GpsPositionTrackerService gpsPositionTrackerService;

    @BeforeEach
    void setUp() {
        gpsPositionTrackerService = new GpsPositionTrackerServiceImpl(eventSender);
    }

    @Test
    void notifyGpsPositionChanged() {
        // Arrange
        var gpsPosition = GpsPosition.builder().build();

        // Act
        gpsPositionTrackerService.notifyGpsPositionChanged(gpsPosition);

        // Assert
        verify(eventSender).send(gpsPosition);
    }

    @Test
    void skipGpsPositionChanged() {
        // Act
        gpsPositionTrackerService.notifyGpsPositionChanged(null);

        // Assert
        verify(eventSender, never()).send(null);
    }
}
