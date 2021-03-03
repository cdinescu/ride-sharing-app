package com.ridesharing.domain.model.ride.events;

import com.ridesharing.domain.model.ride.RideDto;
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
