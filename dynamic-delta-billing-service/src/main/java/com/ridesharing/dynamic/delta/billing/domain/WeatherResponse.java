package com.ridesharing.dynamic.delta.billing.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponse {
    private Float temperature;

    @JsonProperty("wind_speed")
    private Float windSpeed;

    @JsonProperty("wind_degree")
    private Float windDegree;

    @JsonProperty("wind_dir")
    private String windDir;

    private Float pressure;

    @JsonProperty("precip")
    private Float precipitation;

    private Float humidity;

    @JsonProperty("cloudcover")
    private Integer cloudCover;

    private Float visibility;

    @JsonProperty("is_day")
    private String isDay;
}
