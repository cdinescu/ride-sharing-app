package ride.sharing.app.rideplannerservice.eventsender;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ride.sharing.app.rideplannerservice.domain.Ride;
import ride.sharing.app.rideplannerservice.events.EventType;

@Slf4j
@Component
@Profile("!prod & !integration")
public class NoOpEventSender implements EventSender {
    @Override
    public void send(EventType eventType, Ride ride) {
        log.info("Skip send event '{}' for ride '{}' in non production/integration environment", eventType, ride);
    }
}
