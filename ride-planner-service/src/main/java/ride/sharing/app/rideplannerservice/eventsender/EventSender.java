package ride.sharing.app.rideplannerservice.eventsender;


import com.ridesharing.rideplannerservice.RideDto;
import com.ridesharing.rideplannerservice.events.EventType;

public interface EventSender {
    void send(EventType eventType, RideDto ride);
}
