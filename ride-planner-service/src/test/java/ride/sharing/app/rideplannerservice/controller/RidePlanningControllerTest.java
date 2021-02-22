package ride.sharing.app.rideplannerservice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import ride.sharing.app.rideplannerservice.data.TestDatabaseInitializer;
import ride.sharing.app.rideplannerservice.domain.Ride;
import ride.sharing.app.rideplannerservice.domain.enums.RideUpdateType;

import static ride.sharing.app.rideplannerservice.data.TestConstants.DESTINATION;
import static ride.sharing.app.rideplannerservice.data.TestConstants.PICKUP_LOCATION;
import static ride.sharing.app.rideplannerservice.data.TestConstants.UPDATED_DESTINATION;
import static ride.sharing.app.rideplannerservice.data.TestConstants.UPDATED_PICKUP_LOCATION;
import static ride.sharing.app.rideplannerservice.data.TestDataProvider.createRideEntity;
import static ride.sharing.app.rideplannerservice.data.TestDataProvider.createRideRequest;
import static ride.sharing.app.rideplannerservice.data.TestDatabaseInitializer.insertIntoDatabase;
import static ride.sharing.app.rideplannerservice.domain.enums.RideStatus.CANCELLED_BY_CLIENT;
import static ride.sharing.app.rideplannerservice.domain.enums.RideStatus.NEW;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RidePlanningControllerTest {

    private static final String BASE_URI = "/rides";

    /**
     * The table is re-created in the setUp phase. Thus the first id is: 1.
     */
    private static final long FIRST_ID = 1L;
    private static final long NOT_FOUND_ID = 100L;


    @Autowired
    private WebTestClient webClient;

    @Autowired
    private DatabaseClient databaseClient;

    @BeforeEach
    void setUp() {
        TestDatabaseInitializer.initDbTestData(databaseClient);
    }

    @Test
    @Rollback
    void getAllRides() {
        // Arrange
        var expectedRide1 = createRideEntity(PICKUP_LOCATION.name(), DESTINATION.name(), NEW);
        var expectedRide2 = createRideEntity(PICKUP_LOCATION.name(), DESTINATION.name(), CANCELLED_BY_CLIENT);

        insertIntoDatabase(databaseClient, expectedRide1);
        expectedRide1.setId(FIRST_ID);

        insertIntoDatabase(databaseClient, expectedRide2);
        expectedRide2.setId(FIRST_ID + 1);

        // Act & Arrange
        webClient.get().uri(BASE_URI)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Ride.class)
                .contains(expectedRide1, expectedRide2);
    }

    @Test
    @Rollback
    void createRide() {
        // Arrange
        var rideRequest = createRideRequest(PICKUP_LOCATION.name(), DESTINATION.name());
        var expectedRide = createRideEntity(PICKUP_LOCATION.name(), DESTINATION.name(), NEW);
        expectedRide.setId(FIRST_ID);

        // Act & Assert
        webClient.post()
                .uri(BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(rideRequest))
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(Ride.class)
                .isEqualTo(expectedRide);

    }

    @Test
    @Rollback
    void updateRide() {
        // Arrange
        var rideRequest = createRideRequest(UPDATED_PICKUP_LOCATION.name(), UPDATED_DESTINATION.name());
        rideRequest.setUpdateType(RideUpdateType.CLIENT_CANCELLATION);

        var initialRide = createRideEntity(PICKUP_LOCATION.name(), DESTINATION.name(), NEW);
        var expectedRide = createRideEntity(UPDATED_PICKUP_LOCATION.name(), UPDATED_DESTINATION.name(), CANCELLED_BY_CLIENT);
        expectedRide.setId(FIRST_ID);

        insertIntoDatabase(databaseClient, initialRide);

        // Act & Arrange
        webClient.put()
                .uri(BASE_URI + "/" + FIRST_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(rideRequest))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Ride.class)
                .isEqualTo(expectedRide);

    }

    @Test
    @Rollback
    void updateRideWhenEntityIsNotFound() {
        // Arrange
        var rideRequest = createRideRequest(UPDATED_PICKUP_LOCATION.name(), UPDATED_DESTINATION.name());
        rideRequest.setUpdateType(RideUpdateType.CLIENT_CANCELLATION);

        var initialRide = createRideEntity(PICKUP_LOCATION.name(), DESTINATION.name(), NEW);
        var expectedRide = new Ride(); // empty ride entity

        insertIntoDatabase(databaseClient, initialRide);

        // Act & Arrange
        webClient.put()
                .uri(BASE_URI + "/" + NOT_FOUND_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(rideRequest))
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody(Ride.class)
                .isEqualTo(expectedRide);

    }
}
