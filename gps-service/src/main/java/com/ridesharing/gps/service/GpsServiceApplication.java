package com.ridesharing.gps.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class GpsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GpsServiceApplication.class, args);
    }

}
