package ride.sharing.app.rideplannerservice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import ride.sharing.app.rideplannerservice.domain.enums.RideStatus;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Ride implements Cloneable {
    @Id
    private Long id;

    @Column("pickup_location")
    private String pickupLocation;

    @Column("destination")
    private String destination;

    @Column("ride_status")
    private RideStatus rideStatus;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
