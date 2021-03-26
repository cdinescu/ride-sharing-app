package ride.sharing.app.rideplannerservice.eventsender;

import com.ridesharing.rideplannerservice.RideDto;
import com.ridesharing.rideplannerservice.events.EventType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile("!prod & !integration")
public class NoOpEventSender implements EventSender {
    @Override
    public void send(EventType eventType, RideDto ride) {
        log.info("Skip send event '{}' for ride '{}' in non production/integration environment", eventType, ride);
    }
}
