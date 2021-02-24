package ride.sharing.app.rideplannerservice.dao.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ride.sharing.app.rideplannerservice.dao.RideDAO;
import ride.sharing.app.rideplannerservice.domain.Ride;
import ride.sharing.app.rideplannerservice.domain.RideRequest;
import ride.sharing.app.rideplannerservice.events.EventType;
import ride.sharing.app.rideplannerservice.eventsender.EventSender;

@Service
public class RidePlanningServiceImpl implements RidePlanningService {
    private final RideDAO rideDAO;

    private final EventSender eventSender;

    public RidePlanningServiceImpl(RideDAO rideDAO, EventSender eventSender) {
        this.rideDAO = rideDAO;
        this.eventSender = eventSender;
    }

    @Transactional
    @Override
    public Mono<Ride> createRide(RideRequest rideRequest) {
        return rideDAO.createRide(rideRequest)
                .doOnNext(ride -> eventSender.send(EventType.RIDE_CREATED, ride));
    }

    @Override
    public Flux<Ride> findAllRides() {
        return rideDAO.findAllRides();
    }

    @Transactional
    @Override
    public Mono<Ride> updateRide(Long rideId, RideRequest updateRideRequest) {
        return rideDAO.updateRide(rideId, updateRideRequest)
                .doOnNext(ride -> eventSender.send(EventType.RIDE_UPDATED, ride));
    }
}
