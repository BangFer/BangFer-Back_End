package com.capstone.BnagFer.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfig {

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.smtp.connectiontimeout}")
    private int connectionTimeout;

    @Value("${spring.mail.smtp.starttls.enable}")
    private boolean starttlsEnable;

    @Value("${spring.mail.smtp.starttls.required}")
    private boolean starttlsRequired;

    @Value("${spring.mail.smtp.auth}")
    private boolean auth;

    @Value("${spring.mail.smtp.timeout}")
    private int timeout;

    @Bean
    public JavaMailSender javaMailService() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

        javaMailSender.setHost(host);
        javaMailSender.setPort(port);
        javaMailSender.setUsername(username);
        javaMailSender.setPassword(password);
        javaMailSender.setJavaMailProperties(getMailProperties()); // 메일 인증 서버 정보
        javaMailSender.setDefaultEncoding("UTF-8");

        return javaMailSender;
    }

    private Properties getMailProperties() {
        Properties properties = new Properties();
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.connectiontimeout", connectionTimeout);
        properties.put("mail.smtp.starttls.enable", starttlsEnable);
        properties.put("mail.smtp.starttls.required", starttlsRequired);
        properties.put("mail.smtp.auth", auth);
        properties.put("mail.smtp.timeout", timeout);

        return properties;
    }
}
