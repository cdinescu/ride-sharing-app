package com.ridesharing.rideplannerservice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RideDto implements Serializable {
    private String pickupLocation;

    private String destination;

    private RideStatus rideStatus;
}
