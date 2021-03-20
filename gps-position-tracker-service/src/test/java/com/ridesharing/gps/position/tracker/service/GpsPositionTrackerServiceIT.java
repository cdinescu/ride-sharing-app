package com.ridesharing.gps.position.tracker.service;

import com.ridesharing.gps.position.tracker.kafka.KafkaConsumer;
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

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

@ActiveProfiles("integration")
@ContextConfiguration(initializers = {GpsPositionTrackerServiceIT.Initializer.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class GpsPositionTrackerServiceIT extends GpsPositionTrackerServiceTest {
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

    @AfterEach
    void tearDown() {
        consumer.setPayload(null);
    }

    @Test
    void notifyGpsPositionChanged() {
        super.notifyGpsPositionChanged();

        checkMessageSentInTopic(SHOULD_BE_FOUND);
    }

    @Test
    void skipGpsPositionChanged() {
        super.skipGpsPositionChanged();

        checkMessageSentInTopic(!SHOULD_BE_FOUND);
    }

    private void checkMessageSentInTopic(boolean shouldBeFound) {
        Awaitility.await().atMost(Duration.of(30, ChronoUnit.SECONDS))
                .until(() -> consumer.getLatch().await(5, TimeUnit.SECONDS));

        var payload = consumer.getPayload();
        if (shouldBeFound) {
            Assertions.assertNotNull(payload);
            Assertions.assertTrue(payload.contains(topic));
        } else {
            Assertions.assertNull(payload);
        }
    }
}
