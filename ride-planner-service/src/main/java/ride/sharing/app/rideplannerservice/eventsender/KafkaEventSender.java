package ride.sharing.app.rideplannerservice.eventsender;

import com.ridesharing.rideplannerservice.RideDto;
import com.ridesharing.rideplannerservice.events.EventType;
import com.ridesharing.rideplannerservice.events.RideEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import ride.sharing.app.rideplannerservice.producer.Producer;

@Slf4j
@Component
@Profile("!test")
@AllArgsConstructor
public class KafkaEventSender implements EventSender {

    private final Producer producer;

    @Override
    public void send(EventType eventType, RideDto ride) {
        log.info("Send event '{}' for ride '{}", eventType, ride);
        var payload = RideEvent.builder().eventType(eventType).ride(ride).build();
        producer.getMySource().output().send(MessageBuilder.withPayload(payload).setHeader(KafkaHeaders.TOPIC, "ride-planning-topic").build());
    }
}
