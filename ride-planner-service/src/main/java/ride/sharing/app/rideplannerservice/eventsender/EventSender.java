package ride.sharing.app.rideplannerservice.eventsender;

import ride.sharing.app.rideplannerservice.domain.Ride;
import ride.sharing.app.rideplannerservice.events.EventType;

public interface EventSender {
    void send(EventType eventType, Ride ride);
}
