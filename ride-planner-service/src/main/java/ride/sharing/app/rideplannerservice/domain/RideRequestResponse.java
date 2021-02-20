package ride.sharing.app.rideplannerservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ride.sharing.app.rideplannerservice.domain.enums.RideStatus;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RideRequestResponse {
    private RideStatus rideStatus;
}
