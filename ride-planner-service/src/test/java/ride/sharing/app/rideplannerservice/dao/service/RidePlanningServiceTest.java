package ride.sharing.app.rideplannerservice.dao.service;

import com.ridesharing.domain.model.ride.RideUpdateType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.test.annotation.Rollback;
import reactor.test.StepVerifier;
import ride.sharing.app.rideplannerservice.data.TestDatabaseInitializerUtils;
import ride.sharing.app.rideplannerservice.domain.Ride;
import ride.sharing.app.rideplannerservice.domain.RideRequest;

import static com.ridesharing.domain.model.ride.RideStatus.CANCELLED_BY_CLIENT;
import static com.ridesharing.domain.model.ride.RideStatus.CANCELLED_BY_DRIVER;
import static com.ridesharing.domain.model.ride.RideStatus.NEW;
import static com.ridesharing.domain.model.ride.RideUpdateType.CLIENT_CANCELLATION;
import static com.ridesharing.domain.model.ride.RideUpdateType.DRIVER_CANCELLATION;
import static ride.sharing.app.rideplannerservice.data.TestConstants.DESTINATION;
import static ride.sharing.app.rideplannerservice.data.TestConstants.PICKUP_LOCATION;
import static ride.sharing.app.rideplannerservice.data.TestConstants.UPDATED_DESTINATION;
import static ride.sharing.app.rideplannerservice.data.TestConstants.UPDATED_PICKUP_LOCATION;
import static ride.sharing.app.rideplannerservice.data.TestDataProviderUtils.compareDatabaseEntryWithResult;
import static ride.sharing.app.rideplannerservice.data.TestDataProviderUtils.createRideEntity;
import static ride.sharing.app.rideplannerservice.data.TestDataProviderUtils.createRideRequest;
import static ride.sharing.app.rideplannerservice.data.TestDatabaseInitializerUtils.insertIntoDatabase;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RidePlanningServiceTest {

    private static final long FIRST_ID = 1L;

    @Autowired
    private RidePlanningService ridePlanningService;

    @Autowired
    private DatabaseClient databaseClient;

    @BeforeEach
    void setUp() {
        TestDatabaseInitializerUtils.initDbTestData(databaseClient);
    }

    @Test
    @Rollback
    void createRide() throws InterruptedException {
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
    void updateRideWhenClientCancels() {
        // Arrange
        var rideRequest = createRideRequest(UPDATED_PICKUP_LOCATION.name(), UPDATED_DESTINATION.name());
        rideRequest.setUpdateType(CLIENT_CANCELLATION);

        var expectedRide = createRideEntity(UPDATED_PICKUP_LOCATION.name(), UPDATED_DESTINATION.name(), CANCELLED_BY_CLIENT);

        // Act & Assert
        updateAndCheck(rideRequest, expectedRide);
    }

    @Test
    @Rollback
    void updateRideWhenDriverCancels() {
        // Arrange
        var rideRequest = createRideRequest(UPDATED_PICKUP_LOCATION.name(), UPDATED_DESTINATION.name());
        rideRequest.setUpdateType(DRIVER_CANCELLATION);

        var expectedRide = createRideEntity(UPDATED_PICKUP_LOCATION.name(), UPDATED_DESTINATION.name(), CANCELLED_BY_DRIVER);

        // Act & Assert
        updateAndCheck(rideRequest, expectedRide);
    }

    @Test
    @Rollback
    void skipRideStatusUpdateNullUpdate() {
        testNoRideStatusUpdate(null);
    }

    @Test
    @Rollback
    void skipRideStatusUpdateWhenRideStatusUnchanged() {
        testNoRideStatusUpdate(RideUpdateType.NO_UPDATE);
    }

    private void testNoRideStatusUpdate(RideUpdateType updateType) {
        // Arrange
        var rideRequest = createRideRequest(UPDATED_PICKUP_LOCATION.name(), UPDATED_DESTINATION.name());
        rideRequest.setUpdateType(updateType); // null update means update skip

        var expectedRide = createRideEntity(UPDATED_PICKUP_LOCATION.name(), UPDATED_DESTINATION.name(), NEW);

        // Act & Assert
        updateAndCheck(rideRequest, expectedRide);
    }

    private void updateAndCheck(RideRequest rideRequest, Ride expectedRide) {
        var initialRideEntity = createRideEntity(PICKUP_LOCATION.name(), DESTINATION.name(), NEW);
        insertIntoDatabase(databaseClient, initialRideEntity);

        // Act
        var rideMono = ridePlanningService.updateRide(FIRST_ID, rideRequest);

        StepVerifier.create(rideMono)
                .assertNext(r -> compareDatabaseEntryWithResult(expectedRide, r))
                .verifyComplete();
    }

}
