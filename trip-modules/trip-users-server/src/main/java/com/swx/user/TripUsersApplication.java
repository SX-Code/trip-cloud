package com.swx.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.swx.user.mapper")
public class TripUsersApplication {
    public static void main(String[] args) {
        SpringApplication.run(TripUsersApplication.class, args);
    }
}
