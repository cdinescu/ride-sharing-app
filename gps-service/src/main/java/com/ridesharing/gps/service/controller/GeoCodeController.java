package com.ridesharing.gps.service.controller;

import com.ridesharing.gps.service.domain.GeoCodeRequest;
import com.ridesharing.gps.service.service.GeoCodeService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/geocode")
public class GeoCodeController {

    private final GeoCodeService geoCodeService;

    public GeoCodeController(GeoCodeService geoCodeService) {
        this.geoCodeService = geoCodeService;
    }

    @PostMapping
    public Flux<String> convertAddressToGeoCode(@RequestBody GeoCodeRequest request) {
        return geoCodeService.convertAddressToGeoCode(request);
    }
}
