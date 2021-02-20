package ride.sharing.app.rideplannerservice.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import ride.sharing.app.rideplannerservice.domain.Ride;

public interface RideRepository extends ReactiveCrudRepository<Ride, Long> {
}
