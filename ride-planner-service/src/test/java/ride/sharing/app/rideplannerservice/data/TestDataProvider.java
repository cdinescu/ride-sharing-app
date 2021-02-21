package ride.sharing.app.rideplannerservice.data;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.springframework.r2dbc.core.DatabaseClient;
import ride.sharing.app.rideplannerservice.domain.Ride;
import ride.sharing.app.rideplannerservice.domain.RideRequest;
import ride.sharing.app.rideplannerservice.domain.enums.RideStatus;

@Slf4j
public class TestDataProvider<T> {

    private TestDataProvider() {
    }

    public static RideRequest createRideRequest(String pickupLocation, String destination) {
        //@formatter:off
        return RideRequest.builder()
                .pickupLocation(pickupLocation)
                .destination(destination)
                .build();
        //@formatter:on
    }

    public static Ride createRideEntity(String pickupLocation, String destination, RideStatus rideStatus) {
        //@formatter:off
        return Ride.builder()
                .pickupLocation(pickupLocation)
                .destination(destination)
                .rideStatus(rideStatus)
                .build();
        //@formatter:on
    }

    public static void compareDatabaseEntryWithResult(Ride expectedRide, Ride ride) {
        try {
            Ride clonedRide = (Ride) expectedRide.clone();
            clonedRide.setId(ride.getId());

            Assertions.assertEquals(ride, clonedRide);
        } catch (CloneNotSupportedException e) {
            log.error("Failed to clone object {}: ", expectedRide, e);
        }
    }

    public static Object insertIntoDatabase(DatabaseClient databaseClient, Ride ride) {
        return databaseClient
                .sql("INSERT INTO RIDE (pickup_location, destination, ride_status) VALUES (:pickup, :destination, :ride_status)")
                .bind("pickup", ride.getPickupLocation())
                .bind("destination", ride.getDestination())
                .bind("ride_status", ride.getRideStatus() == null ? "NEW" : ride.getRideStatus().name())
                .then().block();
    }
}
