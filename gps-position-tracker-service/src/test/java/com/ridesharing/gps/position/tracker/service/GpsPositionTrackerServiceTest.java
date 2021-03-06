package com.ridesharing.gps.position.tracker.service;

import com.ridesharing.gps.position.tracker.eventsender.EventSender;
import com.ridesharing.gpstrackerservice.DeviceType;
import com.ridesharing.gpstrackerservice.GpsPosition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GpsPositionTrackerServiceTest {
    public static final float LATITUDE = 38.897675f;
    public static final float LONGITUDE = -73.9729691f;
    public static final int SPEED = 100;

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
        var gpsPosition = GpsPosition.builder()
                .latitude(LATITUDE)
                .longitude(LONGITUDE)
                .deviceType(DeviceType.CLIENT_DEVICE)
                .speed(SPEED).build();

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
