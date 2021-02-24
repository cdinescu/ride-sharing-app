package ride.sharing.app.rideplannerservice.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ride.sharing.app.rideplannerservice.domain.Ride;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RideEvent implements Serializable {
    private EventType eventType;
    private Ride ride;
}
