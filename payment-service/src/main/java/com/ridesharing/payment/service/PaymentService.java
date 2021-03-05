package com.ridesharing.payment.service;

import com.ridesharing.payment.domain.model.ChargeRequest;
import com.ridesharing.payment.domain.model.ChargeResponse;
import com.stripe.exception.StripeException;
import reactor.core.publisher.Mono;

public interface PaymentService {
    Mono<ChargeResponse> charge(ChargeRequest chargeRequest) throws StripeException;
}
