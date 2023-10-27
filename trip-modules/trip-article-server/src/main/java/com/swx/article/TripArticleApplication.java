package com.swx.article;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class TripArticleApplication {
    public static void main(String[] args) {
        SpringApplication.run(TripArticleApplication.class, args);
    }
}
