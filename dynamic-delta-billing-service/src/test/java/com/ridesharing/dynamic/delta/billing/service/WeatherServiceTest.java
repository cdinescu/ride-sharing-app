package com.ridesharing.dynamic.delta.billing.service;

import com.ridesharing.commons.RestApiProperties;
import com.ridesharing.dynamic.delta.billing.domain.WeatherResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@SpringBootTest
class WeatherServiceTest {

    public static final String GPS_COORDINATES = "38.897675,-77.036547";

    @Autowired
    private RestApiProperties restApiProperties;
    private WeatherService weatherService;

    @BeforeEach
    void setUp() {
        weatherService = new WeatherServiceImpl(restApiProperties);
    }

    @Test
    void getCurrentWeather() {
        Mono<WeatherResponse> resultFlux = weatherService.getCurrentWeather(GPS_COORDINATES);

        StepVerifier.create(resultFlux)
                .assertNext(response -> {
                    log.info("Got: {}", response);
                    assertNotNull(response.getVisibility());
                    assertNotNull(response.getPrecipitation());
                    assertNotNull(response.getIsDay());
                })
                .expectComplete()
                .verify();
    }
}
