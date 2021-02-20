package ride.sharing.app.rideplannerservice.dao.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ride.sharing.app.rideplannerservice.domain.Ride;
import ride.sharing.app.rideplannerservice.domain.RideRequest;

public interface RidePlanningService {

    Mono<Ride> createRide(RideRequest rideRequest);

    Flux<Ride> findAllRides();

    Mono<Ride> updateRide(Long rideId, RideRequest updateRideRequest);
}
