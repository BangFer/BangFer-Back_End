package com.capstone.BnagFer.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import lombok.Getter;

@Getter
@Configuration
public class AppConfig {
    @Value("${spring.url.base}")
    private String baseUrl;
}