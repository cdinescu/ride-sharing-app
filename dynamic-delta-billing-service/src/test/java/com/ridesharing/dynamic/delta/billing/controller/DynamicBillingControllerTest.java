package com.ridesharing.dynamic.delta.billing.controller;

import com.ridesharing.dynamic.delta.billing.domain.WeatherResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DynamicBillingControllerTest {
    private static final String BASE_URI = "/dynamic-billing-service";
    private static final String GPS_COORDINATES = "38.897675,-77.036547";

    @Autowired
    private WebTestClient webClient;


    @Test
    void getCurrentWeather() {
        webClient.get()
                .uri(BASE_URI + "?query=" + GPS_COORDINATES)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(WeatherResponse.class);
    }
}
