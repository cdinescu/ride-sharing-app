package ride.sharing.app.rideplannerservice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ride.sharing.app.rideplannerservice.domain.enums.RideUpdateType;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RideRequest {
    private String pickupLocation;

    private String destination;

    private RideUpdateType updateType;
}
