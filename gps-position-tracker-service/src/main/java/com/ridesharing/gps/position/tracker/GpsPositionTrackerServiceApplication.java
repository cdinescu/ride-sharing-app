package com.ridesharing.gps.position.tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class GpsPositionTrackerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GpsPositionTrackerServiceApplication.class, args);
    }

}
