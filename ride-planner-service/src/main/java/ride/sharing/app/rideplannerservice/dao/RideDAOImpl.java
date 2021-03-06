package ride.sharing.app.rideplannerservice.dao;

import com.ridesharing.rideplannerservice.RideStatus;
import lombok.AllArgsConstructor;
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
import ride.sharing.app.rideplannerservice.domain.opstatus.OperationStatus;
import ride.sharing.app.rideplannerservice.repository.RideRepository;

@Component
@Slf4j
@AllArgsConstructor
public class RideDAOImpl implements RideDAO {

    public static final boolean RIDE_IS_CHANGED = true;
    private final ModelMapper modelMapper;

    private final RideRepository rideRepository;

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
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .map(ride -> updateRide(updateRideRequest, ride))
                .filter(OperationStatus::isChanged)
                .switchIfEmpty(Mono.empty())
                .map(OperationStatus::getRide)
                .flatMap(rideRepository::save);
    }

    private OperationStatus updateRide(RideRequest rideRequest, Ride rideAboutToUpdate) {
        log.info("Update of {} triggered by {}", rideAboutToUpdate, rideRequest);

        var result = new OperationStatus(rideAboutToUpdate, !RIDE_IS_CHANGED);
        var updatedRide = getUpdateRide(rideRequest, rideAboutToUpdate);

        if (!rideAboutToUpdate.equals(updatedRide)) {
            result.setRide(updatedRide);
            result.setChanged(RIDE_IS_CHANGED);
        }

        return result;
    }

    private Ride getUpdateRide(RideRequest rideRequest, Ride rideAboutToUpdate) {
        Ride updatedRide = rideAboutToUpdate.copy();
        updatedRide.setPickupLocation(rideRequest.getPickupLocation());
        updatedRide.setDestination(rideRequest.getDestination());

        changeRideStatusIfNeeded(rideRequest, rideAboutToUpdate);
        updatedRide.setRideStatus(rideAboutToUpdate.getRideStatus());

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
            case CLIENT_CANCELLATION:
                updatableRide.setRideStatus(RideStatus.CANCELLED_BY_CLIENT);
                break;
            case DRIVER_CANCELLATION:
                updatableRide.setRideStatus(RideStatus.CANCELLED_BY_DRIVER);
                break;
            default:
                break;
        }
    }
}
