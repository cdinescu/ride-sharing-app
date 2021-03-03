package com.ridesharing.domain.model.ride;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RideDto implements Cloneable, Serializable  {
    private String pickupLocation;

    private String destination;

    private RideStatus rideStatus;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}