package ride.sharing.app.rideplannerservice.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.test.annotation.Rollback;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ride.sharing.app.rideplannerservice.domain.Ride;
import ride.sharing.app.rideplannerservice.domain.RideRequest;
import ride.sharing.app.rideplannerservice.domain.enums.RideStatus;
import ride.sharing.app.rideplannerservice.repository.RideRepository;

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static ride.sharing.app.rideplannerservice.domain.enums.RideStatus.CANCELLED_BY_CLIENT;
import static ride.sharing.app.rideplannerservice.domain.enums.RideStatus.NEW;
import static ride.sharing.app.rideplannerservice.domain.enums.RideUpdateType.DRIVER_CANCELLATION;

@ExtendWith(MockitoExtension.class)
class RideDAOTest {

    public static final String PICKUP_LOCATION = "source";
    public static final String DESTINATION = "destination";

    public static final String UPDATED_PICKUP_LOCATION = "new source";
    public static final String UPDATED_DESTINATION = "new destination";
    public static final Long ID = 1L;

    @Mock
    private RideRepository repository;

    @InjectMocks
    private RideDAOImpl rideDAO;

    @BeforeEach
    void setUp() {
        this.rideDAO = new RideDAOImpl(new ModelMapper(), repository);
    }

    @Test
    void createRide() {
        // Arrange
        var rideRequest = createRideRequest(PICKUP_LOCATION, DESTINATION);
        var expectedRide = createRideEntity(PICKUP_LOCATION, DESTINATION, NEW);

        when(repository.save(expectedRide)).thenReturn(Mono.just(expectedRide));

        // Act
        var result = rideDAO.createRide(rideRequest);

        // Assert
        StepVerifier.create(result).expectNext(expectedRide).verifyComplete();
    }

    @Test
    void findAllRides() {
        // Arrange
        var expectedRide1 = createRideEntity(PICKUP_LOCATION, DESTINATION, NEW);
        var expectedRide2 = createRideEntity(PICKUP_LOCATION, DESTINATION, CANCELLED_BY_CLIENT);

        when(repository.findAll()).thenReturn(Flux.just(expectedRide1, expectedRide2));

        // Act
        var allRides = rideDAO.findAllRides();

        // Assert
        StepVerifier.create(allRides).expectNext(expectedRide1).expectNext(expectedRide2).verifyComplete();
    }

    @Test
    void updateRide() {
        // Arrange
        var rideRequest = createRideRequest(UPDATED_PICKUP_LOCATION, UPDATED_DESTINATION);
        rideRequest.setUpdateType(DRIVER_CANCELLATION);

        var initialRideEntity = createRideEntity(PICKUP_LOCATION, DESTINATION, NEW);
        var updatedRide = createRideEntity(UPDATED_PICKUP_LOCATION, UPDATED_DESTINATION, CANCELLED_BY_CLIENT);

        when(repository.findById(ID)).thenReturn(Mono.just(initialRideEntity));
        lenient().when(repository.save(Mockito.any(Ride.class))).thenReturn(Mono.just(updatedRide));

        // Act
        var rideMono = rideDAO.updateRide(ID, rideRequest);

        // Assert
        StepVerifier.create(rideMono).expectNextMatches(updatedRide::equals).verifyComplete();
    }

    private RideRequest createRideRequest(String pickupLocation, String destination) {
        //@formatter:off
        return RideRequest.builder()
                .pickupLocation(pickupLocation)
                .destination(destination)
                .build();
        //@formatter:on
    }

    private Ride createRideEntity(String pickupLocation, String destination, RideStatus rideStatus) {
        //@formatter:off
        return Ride.builder()
                .pickupLocation(pickupLocation)
                .destination(destination)
                .rideStatus(rideStatus)
                .build();
        //@formatter:on
    }
}
