package com.ridesharing.gps.service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ridesharing.gps.service.domain.Address;
import com.ridesharing.gps.service.domain.AddressResponse;
import com.ridesharing.gps.service.exception.InvalidGeoCodeRequestException;
import com.ridesharing.gps.service.restapi.config.GpsRestApiProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

import static com.ridesharing.gps.service.domain.RequestParamEnum.API_KEY;
import static com.ridesharing.gps.service.domain.RequestParamEnum.LIMIT;
import static com.ridesharing.gps.service.domain.RequestParamEnum.OUTPUT_FORMAT;
import static com.ridesharing.gps.service.domain.RequestParamEnum.QUERY;

@Slf4j
@Service
public class GeoCodeServiceImpl implements GeoCodeService {

    public static final String RESULT_LIMIT = "1";
    private final GpsRestApiProperties gpsRestApiProperties;

    private final ObjectMapper mapper;

    public GeoCodeServiceImpl(GpsRestApiProperties gpsRestApiProperties) {
        this.gpsRestApiProperties = gpsRestApiProperties;
        this.mapper = new ObjectMapper();
    }

    /**
     * See required parameters here: https://positionstack.com/documentation
     *
     * @param gpsCoordinates the request which is then unmarshalled and sent to the "positionstack" REST API
     * @return Mono publisher of one address
     */
    @Override
    public Mono<Address> convertAddressToGeoCode(String gpsCoordinates) {
        if (Strings.isBlank(gpsCoordinates)) {
            throw new InvalidGeoCodeRequestException(String.format("Improper request: %s", gpsCoordinates));
        }

        log.info("Processing query: {}", gpsCoordinates);
        return gpsRestApiProperties
                .webClient()
                .get()
                .uri(uriBuilder -> buildUri(gpsCoordinates, uriBuilder))
                .retrieve()
                .bodyToMono(String.class)
                .map(this::mapResponseToAddress);

    }

    private Address mapResponseToAddress(String response) {
        try {
            var addressResponse = mapper.readValue(response, AddressResponse.class);
            var addressList = addressResponse.getAddressList();

            if (addressList == null || addressList.isEmpty()) {
                return new Address();
            }

            return addressList.get(0);
        } catch (JsonProcessingException jsonProcessingException) {
            log.error("Failed to process {}:", response, jsonProcessingException);
        }
        return new Address();
    }

    private URI buildUri(String gpsCoordinates, UriBuilder uriBuilder) {
        return uriBuilder
                .queryParam(API_KEY.getParameterName(), gpsRestApiProperties.getApiKey())
                .queryParam(QUERY.getParameterName(), gpsCoordinates)
                .queryParam(LIMIT.getParameterName(), RESULT_LIMIT)
                .queryParam(OUTPUT_FORMAT.getParameterName(), gpsRestApiProperties.getOutputFormat())
                .build();
    }
}
