package ride.sharing.app.rideplannerservice.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RideDAOTest {

    public static final String PICKUP_LOCATION = "source";
    public static final String DESTINATION = "destination";

    @Mock
    private RideRepository repository;

    @InjectMocks
    private RideDAOImpl rideDAO;

    @BeforeEach
    void setUp() {
        this.rideDAO = new RideDAOImpl(new ModelMapper(), repository);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Rollback
    void createRide() {
        // Arrange
        var rideRequest = createRideRequest(PICKUP_LOCATION, DESTINATION);
        var expectedRide = createRideEntity(PICKUP_LOCATION, DESTINATION, RideStatus.NEW);

        when(repository.save(expectedRide)).thenReturn(Mono.just(expectedRide));

        // Act
        Mono<Ride> result = rideDAO.createRide(rideRequest);

        // Assert
        StepVerifier.create(result).expectNext(expectedRide).verifyComplete();
    }

    @Test
    @Rollback
    void findAllRides() {
        // Arrange
        var expectedRide1 = createRideEntity(PICKUP_LOCATION, DESTINATION, RideStatus.NEW);
        var expectedRide2 = createRideEntity(PICKUP_LOCATION, DESTINATION, RideStatus.CANCELLED_BY_CLIENT);

        when(repository.findAll()).thenReturn(Flux.just(expectedRide1, expectedRide2));

        // Act
        Flux<Ride> allRides = rideDAO.findAllRides();

        // Assert
        StepVerifier.create(allRides).expectNext(expectedRide1).expectNext(expectedRide2).expectComplete();
    }

    @Test
    @Rollback
    void updateRide() {
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
