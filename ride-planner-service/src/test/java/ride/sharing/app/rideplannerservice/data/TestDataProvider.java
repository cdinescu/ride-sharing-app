package ride.sharing.app.rideplannerservice.data;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
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
}
