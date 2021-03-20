package com.ridesharing.gps.position.tracker.kafka;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

@Configuration
@EnableKafka
public class KafkaConfig {

    @Bean
    public KafkaConsumer kafkaConsumer() {
        return new KafkaConsumer();
    }
}
