package com.ridesharing.gps.service.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GeoCodeRequest {
    private String query;
    private Integer limit;
    private String outputFormat;
    private String country;
    private String region;
}
