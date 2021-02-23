package ride.sharing.app.rideplannerservice.dao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ride.sharing.app.rideplannerservice.dao.RideDAO;
import ride.sharing.app.rideplannerservice.domain.Ride;
import ride.sharing.app.rideplannerservice.domain.RideRequest;
import ride.sharing.app.rideplannerservice.events.EventType;
import ride.sharing.app.rideplannerservice.events.RideEvent;
import ride.sharing.app.rideplannerservice.producer.Producer;

@Service
public class RidePlanningServiceImpl implements RidePlanningService {
    private final RideDAO rideDAO;

    @Autowired
    private Producer producer;

    public RidePlanningServiceImpl(RideDAO rideDAO) {
        this.rideDAO = rideDAO;
    }

    @Transactional
    @Override
    public Mono<Ride> createRide(RideRequest rideRequest) {
        return rideDAO.createRide(rideRequest)
                .doOnNext(ride -> sendEvent(EventType.RIDE_CREATED, ride));
    }

    @Override
    public Flux<Ride> findAllRides() {
        return rideDAO.findAllRides();
    }

    @Transactional
    @Override
    public Mono<Ride> updateRide(Long rideId, RideRequest updateRideRequest) {
        return rideDAO.updateRide(rideId, updateRideRequest)
                .doOnNext(ride -> sendEvent(EventType.RIDE_UPDATED, ride));
    }

    private void sendEvent(EventType eventType, Ride ride) {
        RideEvent payload = RideEvent.builder().eventType(eventType).ride(ride).build();
        producer.getMySource().output().send(MessageBuilder.withPayload(payload).build());
    }
}
