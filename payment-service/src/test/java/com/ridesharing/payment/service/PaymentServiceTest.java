package com.ridesharing.payment.service;

import com.ridesharing.payment.domain.model.ChargeRequest;
import com.ridesharing.payment.domain.model.ChargeResponse;
import com.ridesharing.payment.testdata.TestDataUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
class PaymentServiceTest {

    public static final String VALID_TOKEN_SOURCE = "tok_visa";
    public static final String INVALID_TOKEN_SOURCE = "token";

    @Autowired
    private PaymentService paymentService;

    @Test
    void charge() {
        // Arrange
        ChargeRequest chargeRequest = TestDataUtils.getChargeRequest(VALID_TOKEN_SOURCE);

        // Act
        Mono<ChargeResponse> responseMono = paymentService.charge(chargeRequest);

        // Assert
        StepVerifier.create(responseMono).assertNext(response -> {
            Assertions.assertEquals("succeeded", response.getStatus());
        }).expectComplete().verify();
    }

    @Test
    void manageStripeException() {
        // Arrange
        ChargeRequest chargeRequest = TestDataUtils.getChargeRequest(INVALID_TOKEN_SOURCE);

        // Act
        Mono<ChargeResponse> responseMono = paymentService.charge(chargeRequest);

        // Assert
        StepVerifier.create(responseMono).expectComplete().verify();
    }

}
