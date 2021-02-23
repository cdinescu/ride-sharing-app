package ride.sharing.app.rideplannerservice.dao;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ride.sharing.app.rideplannerservice.domain.Ride;
import ride.sharing.app.rideplannerservice.domain.RideRequest;
import ride.sharing.app.rideplannerservice.domain.enums.RideStatus;
import ride.sharing.app.rideplannerservice.repository.RideRepository;

@Component
@Slf4j
public class RideDAOImpl implements RideDAO {

    private final ModelMapper modelMapper;

    private final RideRepository rideRepository;

    public RideDAOImpl(ModelMapper modelMapper, RideRepository rideRepository) {
        this.modelMapper = modelMapper;
        this.rideRepository = rideRepository;
    }

    @Override
    public Mono<Ride> createRide(RideRequest rideRequest) {
        Ride ride = modelMapper.map(rideRequest, Ride.class);
        ride.setRideStatus(RideStatus.NEW);

        return rideRepository.save(ride);
    }

    @Override
    public Flux<Ride> findAllRides() {
        return rideRepository.findAll();
    }

    @Transactional
    @Override
    public Mono<Ride> updateRide(Long rideId, RideRequest updateRideRequest) {
        return rideRepository.findById(rideId)
                .map(ride -> updateRide(updateRideRequest, ride))
                .flatMap(rideRepository::save)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    private Ride updateRide(RideRequest rideRequest, Ride rideAboutToUpdate) {
        Ride result;
        log.info("Update of {} triggered by {}", rideAboutToUpdate, rideRequest);

        try {
            result = (Ride) rideAboutToUpdate.clone();
            result.setPickupLocation(rideRequest.getPickupLocation());
            result.setDestination(rideRequest.getDestination());

            changeRideStatusIfNeeded(rideRequest, rideAboutToUpdate);
            result.setRideStatus(rideAboutToUpdate.getRideStatus());
        } catch (CloneNotSupportedException cloneException) {
            result = rideAboutToUpdate;
            log.error("Failed to clone {}: ", rideAboutToUpdate, cloneException);
        }

        return result;
    }

    private void changeRideStatusIfNeeded(RideRequest rideRequest, Ride updatableRide) {
        var updateType = rideRequest.getUpdateType();
        if (updateType == null) {
            log.info("Skip ride status update on {}. Update requested by {}.", updatableRide, rideRequest);
            return;
        }

        updateStatus(rideRequest, updatableRide);
    }

    private void updateStatus(RideRequest rideRequest, Ride updatableRide) {
        switch (rideRequest.getUpdateType()) {
            case NO_UPDATE:
                break;
            case CLIENT_CANCELLATION:
                updatableRide.setRideStatus(RideStatus.CANCELLED_BY_CLIENT);
                break;
            case DRIVER_CANCELLATION:
                updatableRide.setRideStatus(RideStatus.CANCELLED_BY_DRIVER);
                break;
        }
    }
}
