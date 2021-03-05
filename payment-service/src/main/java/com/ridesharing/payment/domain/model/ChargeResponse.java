package com.ridesharing.payment.domain.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ChargeResponse {
    BigDecimal amount;
    BigDecimal amountCaptured;
    String status;
}
