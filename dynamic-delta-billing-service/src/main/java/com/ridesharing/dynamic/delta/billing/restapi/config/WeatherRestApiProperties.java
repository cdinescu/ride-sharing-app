package com.ridesharing.dynamic.delta.billing.restapi.config;

import com.ridesharing.commons.RestApiProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class WeatherRestApiProperties extends RestApiProperties {

    public WeatherRestApiProperties(@Value("${rest.api.endpoint}") String apiEndpoint,
                                    @Value("${rest.api.key}") String apiKey,
                                    @Value("${rest.api.output}") String outputFormat) {

        super(apiEndpoint, apiKey, outputFormat);
    }
}
