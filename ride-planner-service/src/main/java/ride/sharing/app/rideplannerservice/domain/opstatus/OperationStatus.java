package ride.sharing.app.rideplannerservice.domain.opstatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import ride.sharing.app.rideplannerservice.domain.Ride;

@AllArgsConstructor
@Data
public class OperationStatus {
    private Ride ride;

    private boolean changed;
}
