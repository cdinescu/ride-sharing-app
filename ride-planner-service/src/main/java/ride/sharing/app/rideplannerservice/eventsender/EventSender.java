package ride.sharing.app.rideplannerservice.eventsender;

import com.ridesharing.domain.model.ride.RideDto;
import com.ridesharing.domain.model.ride.events.EventType;

public interface EventSender {
    void send(EventType eventType, RideDto ride);
}
