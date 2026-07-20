package com.example.catalogue.product.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record Product(
        UUID id,
        String name,
        String description,
        BigDecimal price,
        boolean active,
        boolean moodleSyncEnabled,
        Instant createdAt,
        Instant updatedAt
) {

    public Product {
        if (id == null) {
            throw new IllegalArgumentException("Product id is required");
        }
        name = normalizeName(name);
        description = normalizeDescription(description);
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Product price must be zero or positive");
        }
        if (createdAt == null) {
            throw new IllegalArgumentException("Product creation date is required");
        }
        if (updatedAt == null) {
            throw new IllegalArgumentException("Product update date is required");
        }
    }

    public static Product create(UUID id, String name, String description, BigDecimal price, boolean moodleSyncEnabled, Instant now) {
        return new Product(id, name, description, price, false, moodleSyncEnabled, now, now);
    }

    public Product updateDetails(String name, String description, BigDecimal price, boolean moodleSyncEnabled, Instant now) {
        return new Product(id, name, description, price, active, moodleSyncEnabled, createdAt, now);
    }

    public Product activate(Instant now) {
        if (active) {
            return this;
        }
        return new Product(id, name, description, price, true, moodleSyncEnabled, createdAt, now);
    }

    public Product deactivate(Instant now) {
        if (!active) {
            return this;
        }
        return new Product(id, name, description, price, false, moodleSyncEnabled, createdAt, now);
    }

    private static String normalizeName(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Product name is required");
        }
        return value.trim();
    }

    private static String normalizeDescription(String value) {
        return value == null ? "" : value.trim();
    }
}