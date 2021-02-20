package ride.sharing.app.rideplannerservice.dao;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
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

        // TODO generate EVENT: NEW_RIDE
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
                .flatMap(updatedRide -> {
                    log.info("SAVE {}", updatedRide);
                    return rideRepository.save(updatedRide);
                });
    }

    private Ride updateRide(RideRequest rideRequest, Ride rideAboutToUpdate) {
        Ride updatedRide;
        log.info("RideRequest: {}, update ride {}}", rideRequest, rideAboutToUpdate);

        try {
            updatedRide = (Ride) rideAboutToUpdate.clone();
            updatedRide.setPickupLocation(rideRequest.getPickupLocation());
            updatedRide.setDestination(rideRequest.getDestination());

            changeRideStatusIfNeeded(rideRequest, rideAboutToUpdate);
        } catch (CloneNotSupportedException cloneException) {
            updatedRide = rideAboutToUpdate;
            log.error("Failed to clone {}: ", cloneException);
        }

        return updatedRide;
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
