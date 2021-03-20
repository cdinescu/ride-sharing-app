package com.ridesharing.domain.model.ride.gps.position;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class GpsPosition {
    private String trackedDeviceId;

    private DeviceType deviceType;

    private Float latitude;

    private Float longitude;

    private Integer speed;
}
