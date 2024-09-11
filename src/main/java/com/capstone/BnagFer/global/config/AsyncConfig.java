package com.capstone.BnagFer.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(Runtime.getRuntime().availableProcessors() + 1);
        executor.setMaxPoolSize((Runtime.getRuntime().availableProcessors() + 1) * 2);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("WebAppAsync-");
        executor.initialize();
        return executor;
    }
}