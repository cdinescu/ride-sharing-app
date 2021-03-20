package com.ridesharing.dynamic.delta.billing.controller;

import com.ridesharing.dynamic.delta.billing.domain.WeatherResponse;
import com.ridesharing.dynamic.delta.billing.service.WeatherService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/dynamic-billing-service")
@AllArgsConstructor
public class DynamicBillingController {
    private final WeatherService weatherService;

    @GetMapping(produces = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE
    })
    public Mono<WeatherResponse> getCurrentWeather(@RequestParam("query") String queryLocation) {
        return weatherService.getCurrentWeather(queryLocation);
    }
}
