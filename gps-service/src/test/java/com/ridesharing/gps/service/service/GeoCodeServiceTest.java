package com.ridesharing.gps.service.service;

import com.ridesharing.commons.RestApiProperties;
import com.ridesharing.gps.service.domain.Address;
import com.ridesharing.gps.service.exception.InvalidGeoCodeRequestException;
import com.ridesharing.gps.service.testdata.TestDataUtils;
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
    public static final String INVALID_QUERY = "A,B";

    @Autowired
    private RestApiProperties gpsRestApiProperties;
    private GeoCodeService geoCodeService;

    @BeforeEach
    void setUp() {
        geoCodeService = new GeoCodeServiceImpl(gpsRestApiProperties);
    }

    @Test
    void testNullRequest() {
        assertThrows(InvalidGeoCodeRequestException.class,
                () -> geoCodeService.convertAddressToGeoCode(null));
    }

    @Test
    void testMissingQuery() {
        assertThrows(InvalidGeoCodeRequestException.class,
                () -> geoCodeService.convertAddressToGeoCode(EMPTY_QUERY));
    }

    @Test
    void convertAddressToGeoCode() {
        Mono<Address> resultFlux = geoCodeService.convertAddressToGeoCode(GPS_COORDINATES);

        StepVerifier.create(resultFlux)
                .expectNext(TestDataUtils.generateAddressResult())
                .expectComplete()
                .verify();
    }

    @Test
    void checkInvalidQuery() {
        Mono<Address> resultFlux = geoCodeService.convertAddressToGeoCode(INVALID_QUERY);

        StepVerifier.create(resultFlux)
                .expectError()
                .verify();
    }
}
