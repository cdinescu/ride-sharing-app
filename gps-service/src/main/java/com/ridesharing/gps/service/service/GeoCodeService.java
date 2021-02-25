package com.ridesharing.gps.service.service;

import com.ridesharing.gps.service.domain.GeoCodeRequest;
import reactor.core.publisher.Flux;

public interface GeoCodeService {

    Flux<String> convertAddressToGeoCode(GeoCodeRequest request);
}
