package ride.sharing.app.rideplannerservice.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ride.sharing.app.rideplannerservice.domain.Ride;
import ride.sharing.app.rideplannerservice.repository.RideRepository;

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static ride.sharing.app.rideplannerservice.data.TestConstants.DESTINATION;
import static ride.sharing.app.rideplannerservice.data.TestConstants.PICKUP_LOCATION;
import static ride.sharing.app.rideplannerservice.data.TestConstants.UPDATED_DESTINATION;
import static ride.sharing.app.rideplannerservice.data.TestConstants.UPDATED_PICKUP_LOCATION;
import static ride.sharing.app.rideplannerservice.data.TestDataProvider.createRideEntity;
import static ride.sharing.app.rideplannerservice.data.TestDataProvider.createRideRequest;
import static ride.sharing.app.rideplannerservice.domain.enums.RideStatus.CANCELLED_BY_CLIENT;
import static ride.sharing.app.rideplannerservice.domain.enums.RideStatus.NEW;
import static ride.sharing.app.rideplannerservice.domain.enums.RideUpdateType.CLIENT_CANCELLATION;


@ExtendWith(MockitoExtension.class)
class RideDAOTest {

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
        var rideRequest = createRideRequest(PICKUP_LOCATION.name(), DESTINATION.name());
        var expectedRide = createRideEntity(PICKUP_LOCATION.name(), DESTINATION.name(), NEW);

        when(repository.save(expectedRide)).thenReturn(Mono.just(expectedRide));

        // Act
        var rideMono = rideDAO.createRide(rideRequest);

        // Assert
        StepVerifier.create(rideMono).expectNext(expectedRide).verifyComplete();
    }

    @Test
    void findAllRides() {
        // Arrange
        var expectedRide1 = createRideEntity(PICKUP_LOCATION.name(), DESTINATION.name(), NEW);
        var expectedRide2 = createRideEntity(PICKUP_LOCATION.name(), DESTINATION.name(), CANCELLED_BY_CLIENT);

        when(repository.findAll()).thenReturn(Flux.just(expectedRide1, expectedRide2));

        // Act
        var allRidesFlux = rideDAO.findAllRides();

        // Assert
        StepVerifier.create(allRidesFlux).expectNext(expectedRide1).expectNext(expectedRide2).verifyComplete();
    }

    @Test
    void updateRide() {
        // Arrange
        var rideRequest = createRideRequest(UPDATED_PICKUP_LOCATION.name(), UPDATED_DESTINATION.name());
        rideRequest.setUpdateType(CLIENT_CANCELLATION);

        var initialRideEntity = createRideEntity(PICKUP_LOCATION.name(), DESTINATION.name(), NEW);
        var updatedRide = createRideEntity(UPDATED_PICKUP_LOCATION.name(), UPDATED_DESTINATION.name(), CANCELLED_BY_CLIENT);

        when(repository.findById(ID)).thenReturn(Mono.just(initialRideEntity));
        lenient().when(repository.save(Mockito.any(Ride.class))).thenReturn(Mono.just(updatedRide));

        // Act
        var rideMono = rideDAO.updateRide(ID, rideRequest);

        // Assert
        StepVerifier.create(rideMono).expectNextMatches(updatedRide::equals).verifyComplete();
    }
}
