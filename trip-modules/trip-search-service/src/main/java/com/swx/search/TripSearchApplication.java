package com.swx.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class TripSearchApplication {
    public static void main(String[] args) {
        SpringApplication.run(TripSearchApplication.class, args);
    }
}
