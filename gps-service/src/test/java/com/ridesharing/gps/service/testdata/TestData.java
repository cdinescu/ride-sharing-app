package com.ridesharing.gps.service.testdata;

import com.ridesharing.gps.service.domain.Address;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestData {

    public static Address generateAddressResult() {
        return Address.builder()
                .continent("North America")
                .countryCode("USA")
                .country("United States")
                .county("New York County")
                .latitude(40.76384f)
                .longitude(-73.97297f)
                .locality("New York")
                .neighbourhood("Midtown East")
                .number(767)
                .postalCode("10153")
                .region("New York")
                .street("5th Avenue")
                .build();
    }
}
