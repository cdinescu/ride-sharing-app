package ride.sharing.app.rideplannerservice.domain;

import java.time.LocalDateTime;

public class ProcessedRideResponse extends RideRequestResponse {
    private String driverName;

    private String carPlatesNumber;

    private LocalDateTime estimatedArrivalDate;
}
