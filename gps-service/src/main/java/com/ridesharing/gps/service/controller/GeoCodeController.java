package com.ridesharing.gps.service.controller;

import com.ridesharing.gps.service.domain.Address;
import com.ridesharing.gps.service.service.GeoCodeService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/geocode")
public class GeoCodeController {

    private final GeoCodeService geoCodeService;

    public GeoCodeController(GeoCodeService geoCodeService) {
        this.geoCodeService = geoCodeService;
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE})
    public Mono<Address> convertAddressToGeoCode(@RequestParam("query") String gpsCoordinates) {
        return geoCodeService.convertAddressToGeoCode(gpsCoordinates);
    }
}
