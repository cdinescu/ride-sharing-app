package ride.sharing.app.rideplannerservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ride.sharing.app.rideplannerservice.domain.enums.RideStatus;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProcessedRideResponse {
    private String driverName;

    private String carPlatesNumber;

    private LocalDateTime estimatedArrivalDate;

    private RideStatus rideStatus;
}
