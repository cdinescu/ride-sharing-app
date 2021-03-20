package com.ridesharing.payment.controller;

import com.ridesharing.payment.domain.model.ChargeRequest;
import com.ridesharing.payment.domain.model.ChargeResponse;
import com.ridesharing.payment.testdata.TestDataUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PaymentControllerTest {
    private static final String BASE_URI = "/payments";
    private static final String VALID_TOKEN_SOURCE = "tok_visa";
    private static final String INVALID_TOKEN_SOURCE = "token";

    @Autowired
    private WebTestClient webClient;

    @Test
    void charge() {
        ChargeRequest chargeRequest = TestDataUtils.getChargeRequest(VALID_TOKEN_SOURCE);

        webClient.post()
                .uri(BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(chargeRequest))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(ChargeResponse.class)
                .consumeWith(c -> Assertions.assertEquals("succeeded", c.getResponseBody().getStatus()));
    }

    @Test
    void manageStripeException() {
        // Arrange
        ChargeRequest chargeRequest = TestDataUtils.getChargeRequest(INVALID_TOKEN_SOURCE);

        webClient.post()
                .uri(BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(chargeRequest))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(ChargeResponse.class)
                .consumeWith(c -> System.out.println(c.getStatus()));

    }
}
