package com.swx.article.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class AppConfig {

    @Bean
    public ThreadPoolExecutor bizThreadPoolExecutor() {
        // 创建线程池的方式
        /*
          1. Executors 创建，不推荐，
          默认创建的工作队列，使用的是 LinkedBlockingDeque 队列，且默认容量为 Integer 的最大值
          工作队列的容量过大，会导致核心线程工作过载，对垒中任务数过多，且非核心线程无法参与处理，最终导致内存溢出
          Executors.newCachedThreadPool(50);
         */

        // 2. 直接new ThreadPoolExecutor 对象 推荐
        return new ThreadPoolExecutor(10, 50, 10, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(100));
    }
}
