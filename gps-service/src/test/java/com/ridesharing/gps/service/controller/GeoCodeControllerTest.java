package com.ridesharing.gps.service.controller;

import com.ridesharing.gps.service.testdata.TestDataUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GeoCodeControllerTest {
    private static final String BASE_URI = "/geocode";

    @Autowired
    private WebTestClient webClient;

    @Test
    public void statusWhenNullQuery() {
        webClient.get()
                .uri(BASE_URI)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @Test
    public void statusWhenEmptyQuery() {
        webClient.get().uri("/geocode?query=")
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @Test
    public void statusWhenQueryIsOk() {
        webClient.get().uri("/geocode?query=40.7638435,-73.9729691")
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .equals(TestDataUtils.generateAddressResult());
    }

    @Test
    @Disabled
    public void checkInvalidQuery() {
        webClient.get().uri("/geocode?query=A,B")
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
    }

}
