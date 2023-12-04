package com.tzmall.operations.test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public TokenBucket tokenBucket() {
        int capacity = 500; // 设置令牌桶容量
        int refillRate = 20; // 设置填充速率
        return new TokenBucket(capacity, refillRate);
    }
}