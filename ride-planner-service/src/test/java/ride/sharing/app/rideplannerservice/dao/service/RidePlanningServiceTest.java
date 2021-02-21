package ride.sharing.app.rideplannerservice.dao.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.test.annotation.Rollback;
import reactor.core.publisher.Hooks;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static ride.sharing.app.rideplannerservice.data.TestConstants.DESTINATION;
import static ride.sharing.app.rideplannerservice.data.TestConstants.PICKUP_LOCATION;
import static ride.sharing.app.rideplannerservice.data.TestConstants.UPDATED_DESTINATION;
import static ride.sharing.app.rideplannerservice.data.TestConstants.UPDATED_PICKUP_LOCATION;
import static ride.sharing.app.rideplannerservice.data.TestDataProvider.compareDatabaseEntryWithResult;
import static ride.sharing.app.rideplannerservice.data.TestDataProvider.createRideEntity;
import static ride.sharing.app.rideplannerservice.data.TestDataProvider.createRideRequest;
import static ride.sharing.app.rideplannerservice.data.TestDataProvider.insertIntoDatabase;
import static ride.sharing.app.rideplannerservice.domain.enums.RideStatus.CANCELLED_BY_CLIENT;
import static ride.sharing.app.rideplannerservice.domain.enums.RideStatus.NEW;
import static ride.sharing.app.rideplannerservice.domain.enums.RideUpdateType.CLIENT_CANCELLATION;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RidePlanningServiceTest {

    public static final long FIRST_ID = 1L;
    @Autowired
    RidePlanningService ridePlanningService;

    @Autowired
    DatabaseClient databaseClient;

    @BeforeEach
    void setUp() {
        Hooks.onOperatorDebug();

        List<String> statements = Arrays.asList(//
                "DROP TABLE IF EXISTS ride;",
                "CREATE TABLE ride ( id SERIAL PRIMARY KEY, pickup_location VARCHAR(100), destination VARCHAR(100), ride_status VARCHAR(100));");

        statements.forEach(it -> databaseClient.sql(it)
                .fetch()
                .rowsUpdated()
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete());
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Rollback
    void createRide() {
        // Arrange
        var rideRequest = createRideRequest(PICKUP_LOCATION.name(), DESTINATION.name());
        var expectedRide = createRideEntity(PICKUP_LOCATION.name(), DESTINATION.name(), NEW);

        // Act
        var rideMono = ridePlanningService.createRide(rideRequest);

        // Assert
        StepVerifier.create(rideMono)
                .assertNext(ride -> compareDatabaseEntryWithResult(expectedRide, ride))
                .verifyComplete();
    }

    @Test
    @Rollback
    void findAllRides() {
        // Arrange
        var expectedRide1 = createRideEntity(PICKUP_LOCATION.name(), DESTINATION.name(), NEW);
        var expectedRide2 = createRideEntity(PICKUP_LOCATION.name(), DESTINATION.name(), CANCELLED_BY_CLIENT);

        insertIntoDatabase(databaseClient, expectedRide1);
        insertIntoDatabase(databaseClient, expectedRide2);

        // Act
        var allRidesFlux = ridePlanningService.findAllRides();

        // Assert
        StepVerifier.create(allRidesFlux)
                .assertNext(ride -> compareDatabaseEntryWithResult(expectedRide1, ride))
                .assertNext(ride -> compareDatabaseEntryWithResult(expectedRide2, ride))
                .verifyComplete();
    }

    @Test
    @Rollback
    void updateRide() {
        // Arrange
        var rideRequest = createRideRequest(UPDATED_PICKUP_LOCATION.name(), UPDATED_DESTINATION.name());
        rideRequest.setUpdateType(CLIENT_CANCELLATION);

        var initialRideEntity = createRideEntity(PICKUP_LOCATION.name(), DESTINATION.name(), NEW);
        var expectedRide = createRideEntity(UPDATED_PICKUP_LOCATION.name(), UPDATED_DESTINATION.name(), CANCELLED_BY_CLIENT);

        insertIntoDatabase(databaseClient, initialRideEntity);

        // Act
        var rideMono = ridePlanningService.updateRide(FIRST_ID, rideRequest);

        StepVerifier.create(rideMono).assertNext(r -> Assertions.assertEquals(expectedRide, r))/*expectNextMatches(expectedRide::equals)*/.verifyComplete();
    }

}
