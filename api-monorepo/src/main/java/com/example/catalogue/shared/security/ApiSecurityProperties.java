package com.example.catalogue.shared.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.security")
public record ApiSecurityProperties(boolean enabled) {
}