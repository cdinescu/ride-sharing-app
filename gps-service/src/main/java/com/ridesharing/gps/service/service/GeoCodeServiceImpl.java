package com.ridesharing.gps.service.service;

import com.ridesharing.gps.service.domain.GeoCodeRequest;
import com.ridesharing.gps.service.domain.RequestParamEnum;
import com.ridesharing.gps.service.exception.InvalidGeoCodeRequestException;
import com.ridesharing.gps.service.restapi.config.GpsRestApiProperties;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class GeoCodeServiceImpl implements GeoCodeService {

    private final GpsRestApiProperties gpsRestApiProperties;

    public GeoCodeServiceImpl(GpsRestApiProperties gpsRestApiProperties) {
        this.gpsRestApiProperties = gpsRestApiProperties;
    }

    /**
     * See required parameters here: https://positionstack.com/documentation
     *
     * @param request the request which is then unmarshalled and sent to the "positionstack" REST API
     * @return
     */
    @Override
    public Flux<String> convertAddressToGeoCode(GeoCodeRequest request) {
        if (request == null || Strings.isBlank(request.getQuery())) {
            throw new InvalidGeoCodeRequestException(String.format("Improper request: %s", request));
        }

        System.out.println("Request: " + request);
        return gpsRestApiProperties.webClient().get().uri(uriBuilder ->
                uriBuilder.queryParam(RequestParamEnum.API_KEY.getParameterName(), gpsRestApiProperties.getApiKey())
                        .queryParam(RequestParamEnum.QUERY.getParameterName(), request.getQuery())
                        .build()
        ).retrieve().bodyToFlux(String.class).doOnNext(e -> {
            System.out.println("Uite: " + e);
        });

    }
}
