package com.ridesharing.payment.controller;

import com.ridesharing.payment.domain.model.ChargeRequest;
import com.ridesharing.payment.domain.model.ChargeResponse;
import com.ridesharing.payment.service.PaymentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/payments")
@AllArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    public Mono<ChargeResponse> charge(@RequestBody ChargeRequest chargeRequest) {
        log.info("Request: {}", chargeRequest);
        Mono<ChargeResponse> chargeResponseMono = paymentService.charge(chargeRequest).doOnNext(response -> log.info("Got: {}", response));
        log.info("Sent request...");
        return chargeResponseMono;
    }
}
