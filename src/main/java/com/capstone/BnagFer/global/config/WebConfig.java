package com.capstone.BnagFer.global.config;

import com.capstone.BnagFer.domain.report.converter.ReportActivityConverter;
import com.capstone.BnagFer.domain.report.converter.UserActivityConverter;
import com.capstone.BnagFer.global.annotation.LoginUserArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.stream.Stream;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final LoginUserArgumentResolver loginUserArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginUserArgumentResolver);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        Stream.of(
                        new UserActivityConverter(),
                        new ReportActivityConverter())
                .forEach(registry::addConverter);
    }
}