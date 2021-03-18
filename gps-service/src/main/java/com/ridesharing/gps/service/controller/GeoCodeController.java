package com.ridesharing.gps.service.controller;

import com.ridesharing.gps.service.domain.Address;
import com.ridesharing.gps.service.service.GeoCodeService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/geocode")
@AllArgsConstructor
public class GeoCodeController {

    private final GeoCodeService geoCodeService;

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE})
    public Mono<Address> convertAddressToGeoCode(@RequestParam("query") String gpsCoordinates) {
        return geoCodeService.convertAddressToGeoCode(gpsCoordinates);
    }
}
