package com.ridesharing.dynamic.delta.billing.restapi.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@Getter
public class RestApiProperties {
    private final String apiEndpoint;

    private final String apiKey;

    private final String outputFormat;

    public RestApiProperties(@Value("${rest.api.endpoint}") String apiEndpoint,
                             @Value("${rest.api.key}") String apiKey,
                             @Value("${rest.api.output}") String outputFormat) {
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
