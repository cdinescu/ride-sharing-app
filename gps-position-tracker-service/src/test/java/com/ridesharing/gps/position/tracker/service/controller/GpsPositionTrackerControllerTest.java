package com.ridesharing.gps.position.tracker.service.controller;

import com.ridesharing.domain.model.ride.gps.position.GpsPosition;
import com.ridesharing.gps.position.tracker.eventsender.EventSender;
import com.ridesharing.gps.position.tracker.service.GpsPositionTrackerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GpsPositionTrackerControllerTest {

    private static final String BASE_URI = "/gps-receiver";
    public static final float LATITUDE = 38.897675f;
    public static final float LONGITUDE = -73.9729691f;

    @SpyBean
    private EventSender eventSender;

    @Autowired
    private GpsPositionTrackerService gpsPositionTrackerService;

    @Autowired
    private WebTestClient webClient;

    @Test
    void updateGps() {
        // Arrange
        var gpsPosition = GpsPosition.builder()
                .latitude(LATITUDE)
                .longitude(LONGITUDE)
                .build();

        // Act & Assert
        webClient.post()
                .uri(BASE_URI)
                .body(BodyInserters.fromValue(gpsPosition))
                .exchange()
                .expectStatus()
                .isOk();
    }
}
