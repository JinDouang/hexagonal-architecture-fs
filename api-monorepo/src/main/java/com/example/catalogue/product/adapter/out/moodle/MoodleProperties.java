package com.example.catalogue.product.adapter.out.moodle;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.moodle")
public record MoodleProperties(boolean enabled, String baseUrl) {

    public MoodleProperties {
        if (baseUrl == null || baseUrl.isBlank()) {
            baseUrl = "http://localhost:9090";
        }
    }
}