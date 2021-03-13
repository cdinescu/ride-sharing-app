package ride.sharing.app.rideplannerservice.dao.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ride.sharing.app.rideplannerservice.kafka.KafkaConsumer;

import java.util.concurrent.TimeUnit;

@ActiveProfiles("integration")
@ContextConfiguration(initializers = {RidePlanningServiceIT.Initializer.class})
@SpringBootTest
@Testcontainers
public class RidePlanningServiceIT extends RidePlanningServiceTest {

    @Container
    public static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:5.4.3"));;

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.cloud.stream.kafka.binder.brokers=" + kafka.getBootstrapServers(),
                    "spring.kafka.consumer.bootstrap-servers=" + kafka.getBootstrapServers()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    @Autowired
    KafkaConsumer consumer;

    @Test
    void createRide() throws InterruptedException {
        Assertions.assertTrue(kafka.isRunning());

        super.createRide();

        consumer.getLatch().await(10000, TimeUnit.MILLISECONDS);
        Assertions.assertNotNull(consumer.getPayload());
        Assertions.assertTrue(consumer.getPayload().contains("ride-planning-topic"));
    }
}
