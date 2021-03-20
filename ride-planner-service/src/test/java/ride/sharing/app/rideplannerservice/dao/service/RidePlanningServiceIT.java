package ride.sharing.app.rideplannerservice.dao.service;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
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

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

@ActiveProfiles("integration")
@ContextConfiguration(initializers = {RidePlanningServiceIT.Initializer.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class RidePlanningServiceIT extends RidePlanningServiceTest {

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

    @AfterEach
    void tearDown() {
        consumer.setPayload(null);
    }

    @Test
    void createRide() {
        super.createRide();

        checkMessageSentInTopic();
    }

    @Test
    void updateRideWhenClientCancels() {
        super.updateRideWhenClientCancels();

        checkMessageSentInTopic();
    }

    @Test
    void updateRideWhenDriverCancels() {
        super.updateRideWhenDriverCancels();

        checkMessageSentInTopic();
    }

    private void checkMessageSentInTopic() {
        Awaitility.await().atMost(Duration.of(30, ChronoUnit.SECONDS))
                .until(() -> consumer.getLatch().await(5, TimeUnit.SECONDS));

        var payload = consumer.getPayload();
        Assertions.assertNotNull(payload);
        Assertions.assertTrue(payload.contains(topic));

    }
}
