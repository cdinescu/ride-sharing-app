package com.ridesharing.gps.service.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Address {
    @JsonProperty
    private Float latitude;
    @JsonProperty
    private Float longitude;
    @JsonProperty
    private String number;
    @JsonProperty("postal_code")
    private String postalCode;
    @JsonProperty
    private String street;
    @JsonProperty
    private String region;
    @JsonProperty
    private String county;
    @JsonProperty
    private String locality;
    @JsonProperty
    private String neighbourhood;
    @JsonProperty
    private String country;
    @JsonProperty("country_code")
    private String countryCode;
    @JsonProperty
    private String continent;
}
