package com.capstone.BnagFer.global.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Logger;

@Configuration
public class FCMConfig {

    private static final Logger logger = Logger.getLogger(FCMConfig.class.getName());

    @Bean
    FirebaseMessaging firebaseMessaging() throws IOException {
        ClassPathResource resource = new ClassPathResource("/firebase/bangfer-firebase-key.json");

        try (InputStream refreshToken = resource.getInputStream()) {
            FirebaseApp firebaseApp = getOrInitializeFirebaseApp(refreshToken);
            return FirebaseMessaging.getInstance(firebaseApp);
        } catch (IOException e) {
            logger.severe("Failed to initialize FirebaseApp: " + e.getMessage());
            throw e;
        }
    }

    private FirebaseApp getOrInitializeFirebaseApp(InputStream refreshToken) throws IOException {
        List<FirebaseApp> firebaseApps = FirebaseApp.getApps();

        if (firebaseApps != null && !firebaseApps.isEmpty()) {
            for (FirebaseApp app : firebaseApps) {
                if (app.getName().equals(FirebaseApp.DEFAULT_APP_NAME)) {
                    return app;
                }
            }
        }

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(refreshToken))
                .build();

        return FirebaseApp.initializeApp(options);
    }
}
