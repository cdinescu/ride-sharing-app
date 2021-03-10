package com.ridesharing.payment.service;

import com.ridesharing.payment.domain.model.ChargeRequest;
import com.ridesharing.payment.domain.model.ChargeResponse;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {

    private final String secretKey;
    private final Scheduler scheduler;

    public PaymentServiceImpl(@Value("${SECRET_KEY}") String secretKey, Scheduler scheduler) {
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
    public Mono<ChargeResponse> charge(ChargeRequest chargeRequest) {
        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", chargeRequest.getAmount());
        chargeParams.put("currency", chargeRequest.getCurrency());
        chargeParams.put("description", chargeRequest.getDescription());
        chargeParams.put("source", chargeRequest.getStripeToken());

        return Mono.fromCallable(() -> chargePayment(chargeParams))
                .filter(Optional::isPresent)
                .switchIfEmpty(Mono.empty())
                .map(Optional::get)
                .map(this::mapChargeToResponse)
                .subscribeOn(scheduler);
    }

    private Optional<Charge> chargePayment(Map<String, Object> chargeParams) {
        try {
            return Optional.of(Charge.create(chargeParams));
        } catch (StripeException stripeException) {
            log.error("Failed to process charge with params: {} ", chargeParams, stripeException);
        }
        return Optional.empty();
    }

    private ChargeResponse mapChargeToResponse(Charge charge) {
        return ChargeResponse.builder().amount(BigDecimal.valueOf(charge.getAmount()))
                .amountCaptured(BigDecimal.valueOf(charge.getAmountCaptured()))
                .status(charge.getStatus())
                .build();
    }
}
