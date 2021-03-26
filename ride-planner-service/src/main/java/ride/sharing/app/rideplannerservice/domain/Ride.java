package ride.sharing.app.rideplannerservice.domain;

import com.ridesharing.rideplannerservice.RideStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Ride implements Serializable {
    @Id
    private Long id;

    @Column("pickup_location")
    private String pickupLocation;

    @Column("destination")
    private String destination;

    @Column("ride_status")
    private RideStatus rideStatus;

    public Ride copy() {
        return Ride.builder()
                .id(id)
                .pickupLocation(pickupLocation)
                .destination(destination)
                .rideStatus(rideStatus)
                .build();
    }
}
