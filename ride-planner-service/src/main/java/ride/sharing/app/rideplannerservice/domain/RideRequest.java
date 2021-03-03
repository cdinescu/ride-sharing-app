package ride.sharing.app.rideplannerservice.domain;

import com.ridesharing.domain.model.ride.RideUpdateType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RideRequest {
    private String pickupLocation;

    private String destination;

    private RideUpdateType updateType;
}
