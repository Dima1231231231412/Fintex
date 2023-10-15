package com.example.springapp.configs;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class ResilienceConfig {

    // Ограничение на 23 запроса в минуту (в месяц ~ 1.000.000)
    @Bean
    public RateLimiter rateLimiter() {
        RateLimiterConfig config = RateLimiterConfig.custom()
                .limitForPeriod(23)
                .limitRefreshPeriod(Duration.ofMinutes(1))
                .build();
        return RateLimiter.of("weatherApiRateLimiter", config);
    }
}
