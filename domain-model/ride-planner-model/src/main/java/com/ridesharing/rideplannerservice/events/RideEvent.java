package com.ridesharing.rideplannerservice.events;

import com.ridesharing.rideplannerservice.RideDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RideEvent implements Serializable {
    private EventType eventType;
    private RideDto ride;
}
