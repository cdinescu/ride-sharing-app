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
                .county("District of Columbia")
                .latitude(38.897675f)
                .longitude(-77.036547f)
                .locality("Washington")
                .neighbourhood("White House Grounds")
                .number("1600")
                .postalCode("20500")
                .region("District of Columbia")
                .street("Pennsylvania Avenue NW")
                .build();
    }
}
