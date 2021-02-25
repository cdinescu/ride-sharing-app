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
    OUTPUT_FORMAT("output"),
    /**
     * Filter geocoding results by one or multiple comma-separated 2-letter (e.g. AU)
     * or 3-letter country codes (e.g. AUS). Example: country=AU,CA to filter by Australia and Canada.
     */
    COUNTRY("country"),
    /**
     * Filter geocoding results by specifying a region. This could be a neighbourhood, district, city, county,
     * state or administrative area. Example: region=Berlin to filter by locations in Berlin.
     */
    REGION("region");

    private final String parameterName;

    RequestParamEnum(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getParameterName() {
        return parameterName;
    }
}
