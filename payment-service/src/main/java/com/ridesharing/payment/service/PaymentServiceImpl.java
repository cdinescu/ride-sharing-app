package com.ridesharing.payment.service;

import com.ridesharing.payment.domain.model.ChargeRequest;
import com.ridesharing.payment.domain.model.ChargeResponse;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final String secretKey;
    private final Scheduler scheduler;

    public PaymentServiceImpl(@Value("${STRIPE_SECRET_KEY}") String secretKey, Scheduler scheduler) {
        this.secretKey = secretKey;
        this.scheduler = scheduler;
    }

    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }

    /**
     * See Stripe API: https://stripe.com/docs/api?lang=java
     */
    @Override
    public Mono<ChargeResponse> charge(ChargeRequest chargeRequest) throws StripeException {
        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", chargeRequest.getAmount());
        chargeParams.put("currency", chargeRequest.getCurrency());
        chargeParams.put("description", chargeRequest.getDescription());
        chargeParams.put("source", chargeRequest.getStripeToken());

        return Mono.fromCallable(() -> Charge.create(chargeParams)).map(charge ->
                ChargeResponse.builder().amount(BigDecimal.valueOf(charge.getAmount()))
                        .amountCaptured(BigDecimal.valueOf(charge.getAmountCaptured()))
                        .status(charge.getStatus())
                        .build()
        ).subscribeOn(scheduler);
    }
}
