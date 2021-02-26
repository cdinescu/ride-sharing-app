package com.ridesharing.gps.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidGeoCodeRequestException extends RuntimeException {

    public InvalidGeoCodeRequestException(String message) {
        super(message);
    }
}
