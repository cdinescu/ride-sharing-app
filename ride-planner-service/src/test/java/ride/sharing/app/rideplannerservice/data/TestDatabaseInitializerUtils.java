package ride.sharing.app.rideplannerservice.data;

import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Hooks;
import reactor.test.StepVerifier;
import ride.sharing.app.rideplannerservice.domain.Ride;

import java.util.Arrays;
import java.util.List;

public class TestDatabaseInitializerUtils {

    private TestDatabaseInitializerUtils() {
    }

    public static void initDbTestData(DatabaseClient databaseClient) {
        Hooks.onOperatorDebug();

        List<String> statements = Arrays.asList(//
                "DROP TABLE IF EXISTS ride;",
                "CREATE TABLE ride ( id SERIAL PRIMARY KEY, pickup_location VARCHAR(100), destination VARCHAR(100), ride_status VARCHAR(100));");

        statements.forEach(it -> databaseClient.sql(it)
                .fetch()
                .rowsUpdated()
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete());
    }

    public static Object insertIntoDatabase(DatabaseClient databaseClient, Ride ride) {
        return databaseClient
                .sql("INSERT INTO RIDE (pickup_location, destination, ride_status) VALUES (:pickup, :destination, :ride_status)")
                .bind("pickup", ride.getPickupLocation())
                .bind("destination", ride.getDestination())
                .bind("ride_status", ride.getRideStatus() == null ? "NEW" : ride.getRideStatus().name())
                .then().block();
    }
}
