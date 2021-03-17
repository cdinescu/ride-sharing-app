package ride.sharing.app.rideplannerservice.dao.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class RidePlanningServiceIT extends RidePlanningServiceTest {

    public static final boolean SHOULD_BE_FOUND = true;
    @Container
    public static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));

    @Value("${test.topic}")
    private String topic;

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

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();
        kafka.start();
    }

    @AfterEach
    void tearDown() {
        kafka.stop();
    }

    @Test
    void createRide() throws InterruptedException {
        super.createRide();

        checkMessageSentInTopic(SHOULD_BE_FOUND);
    }

    @Test
    void updateRideWhenClientCancels() throws InterruptedException {
        super.updateRideWhenClientCancels();

        checkMessageSentInTopic(SHOULD_BE_FOUND);
    }

    @Test
    void updateRideWhenDriverCancels() throws InterruptedException {
        super.updateRideWhenDriverCancels();

        checkMessageSentInTopic(SHOULD_BE_FOUND);
    }

    @Test
    void skipRideStatusUpdateNullUpdate() throws InterruptedException {
        super.skipRideStatusUpdateNullUpdate();

        checkMessageSentInTopic(!SHOULD_BE_FOUND);
    }

    @Test
    void skipRideStatusUpdateWhenRideStatusUnchanged() throws InterruptedException {
        super.skipRideStatusUpdateWhenRideStatusUnchanged();

        checkMessageSentInTopic(!SHOULD_BE_FOUND);
    }

    private void checkMessageSentInTopic(boolean shouldBeFound) throws InterruptedException {
        consumer.getLatch().await(10000, TimeUnit.MILLISECONDS);
        String payload = consumer.getPayload();

        if (shouldBeFound) {
            Assertions.assertNotNull(payload);
            Assertions.assertTrue(payload.contains(topic));
        } else {
            Assertions.assertNull(payload);
        }
    }
}
