package ride.sharing.app.rideplannerservice.dao.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import ride.sharing.app.rideplannerservice.dao.RideDAO;
import ride.sharing.app.rideplannerservice.repository.RideRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RidePlanningServiceTestIT {

    @Autowired
    RideRepository repository;

    @Autowired
    RideDAO rideDAO;

    @BeforeEach
    void setUp() {
        System.out.println("repository null? " + (repository == null));
        System.out.println("dao null? " + (rideDAO == null));
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Rollback
    void createRide() {
    }

    @Test
    void findAllRides() {
    }

    @Test
    void updateRide() {
    }
}
