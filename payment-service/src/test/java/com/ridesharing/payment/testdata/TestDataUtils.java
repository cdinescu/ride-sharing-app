package com.ridesharing.payment.testdata;

import com.ridesharing.payment.domain.model.ChargeRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Currency;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestDataUtils {

    public static ChargeRequest getChargeRequest(String token) {
        return ChargeRequest.builder()
                .amount(120)
                .currency(Currency.getInstance("USD"))
                .description("Test payment")
                .stripeToken(/*"token"*//*"tok_visa"*/token)
                .stripeEmail("test@mail.com")
                .build();
    }
}
