package com.swx.data;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 启用 Spring 任务调度
 */
@EnableScheduling
@MapperScan("com.swx.data.mapper")
@SpringBootApplication
public class DataApplication {
    public static void main(String[] args) {
        SpringApplication.run(DataApplication.class, args);
    }
}
