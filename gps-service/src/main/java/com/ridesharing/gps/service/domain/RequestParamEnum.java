package com.ridesharing.gps.service.domain;

public enum RequestParamEnum {
    /**
     * API access key
     */
    API_KEY("access_key"),
    /**
     * Specify your query as a free-text address, place name or using any other
     * common text-based location identifier (e.g. postal code, city name, region name).
     */
    QUERY("query"),
    /**
     * Specify a limit between 1 and 80 to limit the number of results returned per geocoding query.
     * Batch requests can contain multiple geocoding queries. (Default: 10 results)
     */
    LIMIT("limit"),
    /**
     * Specify your preferred API output format. Available values: json (default), xml and geojson.
     */
    OUTPUT_FORMAT("output");

    private final String parameterName;

    RequestParamEnum(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getParameterName() {
        return parameterName;
    }
}
