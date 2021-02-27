package com.ridesharing.dynamic.delta.billing.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ridesharing.dynamic.delta.billing.domain.WeatherResponse;
import com.ridesharing.dynamic.delta.billing.restapi.config.RestApiProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

@Slf4j
@Service
public class WeatherServiceImpl implements WeatherService {

    public static final String METRIC_SYSTEM = "m";
    private final RestApiProperties gpsRestApiProperties;

    private final ObjectMapper mapper;

    public WeatherServiceImpl(RestApiProperties gpsRestApiProperties) {
        this.gpsRestApiProperties = gpsRestApiProperties;
        this.mapper = new ObjectMapper();;
    }

    /**
     * See required parameters here: https://weatherstack.com/documentation
     *
     * @param queryLocation the location for which the current weather us needed
     * @return Mono publisher of one weather response
     */
    @Override
    public Mono<WeatherResponse> getCurrentWeather(String queryLocation) {
        log.info("Processing query: {}", queryLocation);
        return gpsRestApiProperties
                .webClient()
                .get()
                .uri(uriBuilder -> buildUri(queryLocation, uriBuilder))
                .retrieve()
                .bodyToMono(String.class)
                .map(this::mapResponseToWeather);
    }

    private URI buildUri(String gpsCoordinates, UriBuilder uriBuilder) {
        return uriBuilder
                .queryParam("access_key", gpsRestApiProperties.getApiKey())
                .queryParam("query", gpsCoordinates)
                .queryParam("units", METRIC_SYSTEM)
                .build();
    }

    private WeatherResponse mapResponseToWeather(String response) {
        try {
            JsonNode jsonNode = mapper.readTree(response);
            JsonNode current = jsonNode.get("current");
            WeatherResponse weatherResponse = mapper.treeToValue(current, WeatherResponse.class);
            return weatherResponse;
        } catch (JsonProcessingException jsonProcessingException) {
            log.error("Failed to process {}:", response, jsonProcessingException);
        }
        return new WeatherResponse();
    }
}
