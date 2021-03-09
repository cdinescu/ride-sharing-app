package com.ridesharing.gps.service.restapi.config;

import com.ridesharing.commons.RestApiProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GpsRestApiProperties extends RestApiProperties {

    public GpsRestApiProperties(@Value("${gps.rest.api.endpoint}") String apiEndpoint,
                                @Value("${gps.rest.api.key}") String apiKey,
                                @Value("${gps.rest.api.output}") String outputFormat) {
        super(apiEndpoint, apiKey, outputFormat);
    }
}
