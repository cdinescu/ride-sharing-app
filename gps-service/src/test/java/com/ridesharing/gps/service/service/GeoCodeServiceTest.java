package com.ridesharing.gps.service.service;

import com.ridesharing.gps.service.domain.Address;
import com.ridesharing.gps.service.exception.InvalidGeoCodeRequestException;
import com.ridesharing.gps.service.restapi.config.GpsRestApiProperties;
import com.ridesharing.gps.service.testdata.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GeoCodeServiceTest {

    public static final String GPS_COORDINATES = "38.897675,-77.036547";
    public static final String EMPTY_QUERY = "";

    @Autowired
    private GpsRestApiProperties gpsRestApiProperties;
    private GeoCodeService geoCodeService;

    @BeforeEach
    void setUp() {
        geoCodeService = new GeoCodeServiceImpl(gpsRestApiProperties);
    }

    @Test
    public void testNullRequest() {
        assertThrows(InvalidGeoCodeRequestException.class,
                () -> geoCodeService.convertAddressToGeoCode(null));
    }

    @Test
    public void testMissingQuery() {
        assertThrows(InvalidGeoCodeRequestException.class,
                () -> geoCodeService.convertAddressToGeoCode(EMPTY_QUERY));
    }

    @Test
    public void convertAddressToGeoCode() {
        Mono<Address> resultFlux = geoCodeService.convertAddressToGeoCode(GPS_COORDINATES);

        StepVerifier.create(resultFlux).expectNext(TestData.generateAddressResult()).expectComplete();
    }
}
