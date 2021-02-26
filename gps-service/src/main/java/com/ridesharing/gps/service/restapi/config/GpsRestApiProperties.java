package com.ridesharing.gps.service.restapi.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@Getter
public class GpsRestApiProperties {
    private final String apiEndpoint;

    private final String apiKey;

    private final String outputFormat;

    public GpsRestApiProperties(@Value("${gps.rest.api.endpoint}") String apiEndpoint,
                                @Value("${gps.rest.api.key}") String apiKey,
                                @Value("${gps.rest.api.output}") String outputFormat) {
        this.apiEndpoint = apiEndpoint;
        this.apiKey = apiKey;
        this.outputFormat = outputFormat;
    }

    public WebClient webClient() {
        return WebClient
                .builder()
                .baseUrl(apiEndpoint)
                .build();
    }
}
