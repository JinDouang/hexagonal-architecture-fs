package com.example.catalogue.product.adapter.in.web;

import com.example.catalogue.product.domain.Product;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ProductResponse(
        UUID id,
        String name,
        String description,
        BigDecimal price,
        boolean active,
        boolean moodleSyncEnabled,
        Instant createdAt,
        Instant updatedAt
) {

    static ProductResponse from(Product product) {
        return new ProductResponse(
                product.id(),
                product.name(),
                product.description(),
                product.price(),
                product.active(),
                product.moodleSyncEnabled(),
                product.createdAt(),
                product.updatedAt()
        );
    }
}