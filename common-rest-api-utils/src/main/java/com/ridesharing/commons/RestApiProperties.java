package com.ridesharing.commons;

import lombok.Getter;
import org.springframework.web.reactive.function.client.WebClient;

@Getter
public class RestApiProperties {
    private final String apiEndpoint;

    private final String apiKey;

    private final String outputFormat;

    public RestApiProperties(String apiEndpoint,
                             String apiKey,
                             String outputFormat) {
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
