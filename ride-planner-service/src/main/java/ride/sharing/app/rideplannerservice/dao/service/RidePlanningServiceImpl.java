package ride.sharing.app.rideplannerservice.dao.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ride.sharing.app.rideplannerservice.dao.RideDAO;
import ride.sharing.app.rideplannerservice.domain.Ride;
import ride.sharing.app.rideplannerservice.domain.RideRequest;

@Service
public class RidePlanningServiceImpl implements  RidePlanningService {
    private final RideDAO rideDAO;

    public RidePlanningServiceImpl(RideDAO rideDAO) {
        this.rideDAO = rideDAO;
    }

    //@Transactional
    @Override
    public Mono<Ride> createRide(RideRequest rideRequest) {
        return rideDAO.createRide(rideRequest);
    }

    @Override
    public Flux<Ride> findAllRides() {
        return rideDAO.findAllRides();
    }

    //@Transactional
    @Override
    public Mono<Ride> updateRide(Long rideId, RideRequest updateRideRequest) {
        return rideDAO.updateRide(rideId, updateRideRequest);
    }
}
