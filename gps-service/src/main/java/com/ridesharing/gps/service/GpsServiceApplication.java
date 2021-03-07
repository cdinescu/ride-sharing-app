package com.ridesharing.gps.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class GpsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GpsServiceApplication.class, args);
    }

}
