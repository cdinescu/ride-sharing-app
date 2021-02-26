package com.ridesharing.gps.service.service;

import com.ridesharing.gps.service.domain.Address;
import reactor.core.publisher.Mono;

public interface GeoCodeService {

    Mono<Address> convertAddressToGeoCode(String gpsCoordinates);
}
