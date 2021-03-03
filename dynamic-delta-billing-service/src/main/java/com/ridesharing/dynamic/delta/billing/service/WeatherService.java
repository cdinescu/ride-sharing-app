package com.ridesharing.dynamic.delta.billing.service;

import com.ridesharing.dynamic.delta.billing.domain.WeatherResponse;
import reactor.core.publisher.Mono;

public interface WeatherService {

    Mono<WeatherResponse> getCurrentWeather(String queryLocation);
}
