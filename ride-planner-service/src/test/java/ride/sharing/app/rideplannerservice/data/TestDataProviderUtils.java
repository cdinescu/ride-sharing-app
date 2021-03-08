package ride.sharing.app.rideplannerservice.data;

import com.ridesharing.domain.model.ride.RideStatus;
import org.junit.jupiter.api.Assertions;
import ride.sharing.app.rideplannerservice.domain.Ride;
import ride.sharing.app.rideplannerservice.domain.RideRequest;

public class TestDataProviderUtils<T> {

    private TestDataProviderUtils() {
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
        Ride clonedRide = expectedRide.copy();
        clonedRide.setId(ride.getId());

        Assertions.assertEquals(ride, clonedRide);
    }
}
