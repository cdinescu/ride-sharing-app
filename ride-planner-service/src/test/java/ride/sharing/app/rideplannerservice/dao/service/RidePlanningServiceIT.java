package ride.sharing.app.rideplannerservice.dao.service;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.concurrent.TimeUnit;

@ActiveProfiles("integration")
@ContextConfiguration(initializers = {RidePlanningServiceIT.Initializer.class})
public class RidePlanningServiceIT extends RidePlanningServiceTest {

    public static KafkaContainer kafka;

    static {
        kafka =
                new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:5.4.3"));
        kafka.start();
    }

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
        super.createRide();

/*        consumer.getLatch().await(10000, TimeUnit.MILLISECONDS);
        Assertions.assertNotNull(consumer.getPayload());
        Assertions.assertTrue(consumer.getPayload().contains("ride-planning-topic"));*/
        ConsumerRecords consumerRecords = consumer.poll(Duration.of(2000, ChronoUnit.MILLIS));

        consumerRecords.forEach(record -> System.out.println(">>>> Am: " + record));
        Assertions.assertFalse(consumerRecords.isEmpty());
    }
}
